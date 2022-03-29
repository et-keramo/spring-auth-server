package et.keramo.authsvr.service;

import et.keramo.authsvr.constants.SecurityConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public abstract class AbstractAuthService extends RestService {

    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_SECRET_KEY = "client_secret";

    public abstract Object login(HttpServletRequest request, HttpServletResponse response) throws Exception;
    public abstract Object validate(HttpServletRequest request, HttpServletResponse response) throws Exception;
    public abstract Object logout(HttpServletRequest request, HttpServletResponse response) throws Exception;

    public String getClientId(HttpServletRequest request) {
        String clientId = request.getParameter(CLIENT_ID_KEY);

        if (clientId == null) {
            String[] credentials = this.getBasicAuthentication(request.getHeader(SecurityConstants.AUTHORIZATION_HEADER));
            clientId = credentials[0];
        }
        return clientId;
    }

    public String getClientSecret(HttpServletRequest request) {
        String clientSecret = request.getParameter(CLIENT_SECRET_KEY);

        if (clientSecret == null) {
            String[] credentials = this.getBasicAuthentication(request.getHeader(SecurityConstants.AUTHORIZATION_HEADER));
            clientSecret = credentials[1];
        }
        return clientSecret;
    }

    // Private
    private String[] getBasicAuthentication(String authorization) {
        String base64Credentials = authorization.replace(SecurityConstants.BASIC_AUTH_PREFIX, "").trim();
        byte[] decoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decoded, StandardCharsets.UTF_8);

        return credentials.split(":", 2);
    }

}
