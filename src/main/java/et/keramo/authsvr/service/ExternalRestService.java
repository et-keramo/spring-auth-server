package et.keramo.authsvr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
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
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Slf4j
public class ExternalRestService {

    public RestTemplate restTemplate;

    @Getter
    @Value("${auth.url}")
    private String authUrl;

    public ExternalRestService() {
        this.restTemplate = new RestTemplate();

        // Error Handler 설정
//        this.restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
//            @Override
//            // Ignore 400
//            public void handleError(ClientHttpResponse response) throws IOException {
//                if (response.getRawStatusCode() != 400) {
//                    super.handleError(response);
//                }
//            }
//        });

        // SSL 설정
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
        return this.getAuthUrl();
    }

    public String makePath(String... paths) {
        StringBuilder path = new StringBuilder(this.getBasePath());

        for (String p : paths) {
            path.append(p);
        }
        return path.toString();
    }

    public HttpHeaders makeHeader() {
        return new HttpHeaders();
    }

    public JsonNode getRequest(String path) {
        return this.request(path, HttpMethod.GET);
    }

    public JsonNode putRequest(String path, Object body) {
        return this.request(path, HttpMethod.PUT, body);
    }

    public JsonNode putRequest(String path) {
        return this.request(path, HttpMethod.PUT);
    }

    public JsonNode postRequest(String path, Object body) {
        return this.request(path, HttpMethod.POST, body);
    }

    public JsonNode postRequest(String path) {
        return this.request(path, HttpMethod.POST);
    }

    public JsonNode deleteRequest(String path) {
        return this.request(path, HttpMethod.DELETE);
    }


    private JsonNode request(String path, HttpMethod method, Object body) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(body, this.makeHeader());
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(path, method, httpEntity, String.class);
        Optional<String> responseBody = Optional.ofNullable(responseEntity.getBody());

        return responseBody.map(o -> {
            try {
                return new ObjectMapper().readTree(o);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Unable to read response");
            }
        }).orElse(null);
    }

    private JsonNode request(String path, HttpMethod method) {
        return this.request(path, method, null);
    }

}
