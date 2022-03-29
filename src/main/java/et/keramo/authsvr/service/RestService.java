package et.keramo.authsvr.service;

import com.google.gson.JsonObject;
import com.metabuild.authsvr.exception.AuthServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

@Slf4j
public class RestService {

    @Value("${auth.url}")
    public String authUrl;

    public RestTemplate restTemplate;

    public RestService() {
        this.restTemplate = new RestTemplate();

        this.restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });

        try {
            TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            this.restTemplate.setRequestFactory(requestFactory);

        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.error("Error configuring HttpClient for RestTemplate", e);
        }
    }

    public String getBasePath() {
        return this.authUrl;
    }

    public String makePath(String... paths) {
        StringBuilder path = new StringBuilder(this.getBasePath());

        for (String p : paths) {
            path.append(p);
        }
        return path.toString();
    }

    public MultiValueMap<String, Object> getRequestData(HttpServletRequest request) {
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        Enumeration<String> keys = request.getParameterNames();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = request.getParameter(key);
            data.add(key, value);
        }
        return data;
    }

    public Object getRequest(String path, HttpHeaders header) throws AuthServerException {
        return this.request(path, HttpMethod.GET, header);
    }

    public Object getRequest(String path) throws AuthServerException {
        return this.request(path, HttpMethod.GET);
    }

    public Object putRequest(String path, HttpHeaders header, JsonObject body) throws AuthServerException {
        return this.request(path, HttpMethod.PUT, header, body);
    }

    public Object putRequest(String path, JsonObject body) throws AuthServerException {
        return this.request(path, HttpMethod.PUT, body);
    }

    public Object putRequest(String path, HttpHeaders header) throws AuthServerException {
        return this.request(path, HttpMethod.PUT, header);
    }

    public Object postRequest(String path, HttpHeaders header, JsonObject body) throws AuthServerException {
        return this.request(path, HttpMethod.POST, header, body);
    }

    public Object postRequest(String path, JsonObject body) throws AuthServerException {
        return this.request(path, HttpMethod.POST, body);
    }

    public Object postRequest(String path, HttpHeaders header, MultiValueMap<String, Object> body) throws AuthServerException {
        return this.request(path, HttpMethod.POST, header, body);
    }

    public Object postRequest(String path, MultiValueMap<String, Object> body) throws AuthServerException {
        return this.request(path, HttpMethod.POST, body);
    }

    public Object deleteRequest(String path, HttpHeaders header) throws AuthServerException {
        return this.request(path, HttpMethod.DELETE, header);
    }

    public Object deleteRequest(String path) throws AuthServerException {
        return this.request(path, HttpMethod.DELETE);
    }

    // Private
    private Object request(String path, HttpMethod method, HttpHeaders header, Object body) throws AuthServerException {
        try {
            return this.restTemplate.exchange(
                    path,
                    method,
                    new HttpEntity<>(body, header),
                    Object.class
            ).getBody();
        } catch (Exception e) {
            throw AuthServerException.create(e);
        }
    }

    private Object request(String path, HttpMethod method, Object body) throws AuthServerException {
        HttpHeaders header = new HttpHeaders();
        return this.request(path, method, header, body);
    }

    private Object request(String path, HttpMethod method, HttpHeaders header) throws AuthServerException {
        return this.request(path, method, header, null);
    }

    private Object request(String path, HttpMethod method) throws AuthServerException {
        HttpHeaders header = new HttpHeaders();
        return this.request(path, method, header, null);
    }

}
