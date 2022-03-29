package et.keramo.authsvr.service.keycloak;

import et.keramo.authsvr.constants.SecurityConstants;
import et.keramo.authsvr.service.AbstractDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public abstract class AbstractKeycloakService extends AbstractDataService {

    private static final String GRANT_TYPE_KEY = "grant_type";
    private static final String CLIENT_ID_KEY = "client_id";
    private static final String USER_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    @Value("${auth.realm}")
    private String realm;

    @Value("${auth.master-realm}")
    private String masterRealm;

    private String token;

    @Override
    public String getBasePath() {
        return this.authUrl + String.format(SecurityConstants.KEYCLOAK_ADMIN_URL_FORMAT, this.realm);
    }

    // Keycloak Authentication
    public void preAuthentication() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE_KEY, "password");
        body.add(CLIENT_ID_KEY, "admin-cli");
        body.add(USER_KEY, "admin");
        body.add(PASSWORD_KEY, "admin");

        String authPath = this.authUrl +
                String.format(SecurityConstants.KEYCLOAK_URL_FORMAT, this.masterRealm) +
                SecurityConstants.KEYCLOAK_LOGIN_PATH;

        Map<String, Object> result = (Map<String, Object>) this.postRequest(authPath, body);
        this.token = (String) result.get("access_token");
    }

    // Make Header & Body
    public HttpHeaders makeHeader() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Authorization", "Bearer " + this.token);
        return header;
    }

}
