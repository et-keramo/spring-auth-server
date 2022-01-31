package et.keramo.authsvr.service.auth;

import et.keramo.authsvr.constants.SecurityConstants;
import et.keramo.authsvr.exception.AuthServerException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Map;

public abstract class AbstractAuthService {

    public RestTemplate restTemplate;

    public AbstractAuthService() {
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
    }

    public abstract Object login(HttpServletRequest request, HttpServletResponse response) throws Exception;
    public abstract Object validate(HttpServletRequest request, HttpServletResponse response) throws Exception;
    public abstract Object logout(HttpServletRequest request, HttpServletResponse response) throws Exception;

    public Object postRequest(String path, HttpHeaders header, MultiValueMap<String, String> formData) throws AuthServerException {
        try {
            return this.restTemplate.exchange(
                    path,
                    HttpMethod.POST,
                    new HttpEntity<>(formData, header),
                    Map.class
            ).getBody();
        } catch (Exception e) {
           throw AuthServerException.create(e);
        }
    }

    public Object postRequest(String path, MultiValueMap<String, String> formData) throws AuthServerException {
        HttpHeaders header = new HttpHeaders();
        return this.postRequest(path, header, formData);
    }

    public String getClientId(HttpServletRequest request) {
        String clientId = request.getParameter("client_id");

        if (clientId == null) {
            String[] credentials = this.getBasicAuthentication(request.getHeader(SecurityConstants.AUTHORIZATION_HEADER));
            clientId = credentials[0];
        }

        return clientId;
    }

    public String getClientSecret(HttpServletRequest request) {
        String clientSecret = request.getParameter("client_secret");

        if (clientSecret == null) {
            String[] credentials = this.getBasicAuthentication(request.getHeader(SecurityConstants.AUTHORIZATION_HEADER));
            clientSecret = credentials[1];
        }

        return clientSecret;
    }

    public MultiValueMap<String, String> getFormData(HttpServletRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        Enumeration<String> keys = request.getParameterNames();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = request.getParameter(key);
            formData.add(key, value);
        }
        return formData;
    }

    // Private
    private String[] getBasicAuthentication(String authorization) {
        String base64Credentials = authorization.replace(SecurityConstants.BASIC_AUTH_PREFIX, "").trim();
        byte[] decoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decoded, StandardCharsets.UTF_8);

        return credentials.split(":", 2);
    }

}
