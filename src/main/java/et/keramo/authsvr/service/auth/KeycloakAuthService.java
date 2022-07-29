package et.keramo.authsvr.service.auth;

import et.keramo.authsvr.annotation.KeycloakAuthorization;
import et.keramo.authsvr.exception.AuthServerException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.Objects;

import static et.keramo.authsvr.constants.keycloak.KeycloakConst.*;

public class KeycloakAuthService extends AuthService {

    @Value("${auth.realm:default}")
    private String realm;

    @Setter
    private String authorization;

    @Override
    public String getBasePath() {
        return this.getAuthUrl() + API_PATH(this.realm);
    }

    @Override
    public HttpHeaders makeHeader() {
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", this.authorization);
        return header;
    }

    @KeycloakAuthorization
    @Override
    public Object login(HttpServletRequest request, HttpServletResponse response) throws AuthServerException {
        MultiValueMap<String, Object> dataMap = this.getRequestData(request);

        if (Objects.equals(dataMap.getFirst("grant_type"), "password")) {
            String userId = Objects.requireNonNull(dataMap.getFirst("user_id")).toString();
            dataMap.remove("user_id");
            dataMap.add("username", userId);
        }

        return this.postRequest(this.makePath(LOGIN_PATH), dataMap);
    }

    @KeycloakAuthorization
    @Override
    public Object validate(HttpServletRequest request, HttpServletResponse response) throws AuthServerException {
        return this.postRequest(this.makePath(VALIDATION_PATH), this.getRequestData(request));
    }

    @KeycloakAuthorization
    @Override
    public Object logout(HttpServletRequest request, HttpServletResponse response) throws AuthServerException {
        return this.postRequest(this.makePath(LOGOUT_PATH), this.getRequestData(request));
    }

}