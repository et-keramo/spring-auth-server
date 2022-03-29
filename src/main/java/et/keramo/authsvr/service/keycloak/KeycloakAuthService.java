package et.keramo.authsvr.service.keycloak;

import et.keramo.authsvr.constants.SecurityConstants;
import et.keramo.authsvr.exception.AuthServerException;
import et.keramo.authsvr.service.AbstractAuthService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@Qualifier("keycloakAuthService")
public class KeycloakAuthService extends AbstractAuthService {

    @Value("${auth.realm}")
    private String realm;

    @Override
    public String getBasePath() {
        return super.getBasePath() + String.format(SecurityConstants.KEYCLOAK_URL_FORMAT, this.realm);
    }

    @Override
    public Object login(HttpServletRequest request, HttpServletResponse response) throws AuthServerException {
        return this.postRequest(
                this.makePath(SecurityConstants.KEYCLOAK_LOGIN_PATH),
                this.getRequestData(request)
        );
    }

    @Override
    public Object validate(HttpServletRequest request, HttpServletResponse response) throws AuthServerException {
        return this.postRequest(
                this.makePath(SecurityConstants.KEYCLOAK_VALIDATION_PATH),
                this.makeHeader(request),
                this.getRequestData(request)
        );
    }

    @Override
    public Object logout(HttpServletRequest request, HttpServletResponse response) throws AuthServerException {
        return this.postRequest(
                this.makePath(SecurityConstants.KEYCLOAK_LOGOUT_PATH),
                this.makeHeader(request),
                this.getRequestData(request)
        );
    }

    private HttpHeaders makeHeader(HttpServletRequest request) {
        HttpHeaders header = new HttpHeaders();
        String auth = String.format("%s:%s", this.getClientId(request), this.getClientSecret(request));
        header.set("Authorization", "Basic " + new String(Base64.encode(auth.getBytes(StandardCharsets.UTF_8))));
        return header;
    }

}