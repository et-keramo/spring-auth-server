package et.keramo.authsvr.service.auth.keycloak;

import et.keramo.authsvr.constants.SecurityConstants;
import et.keramo.authsvr.exception.AuthServerException;
import et.keramo.authsvr.service.auth.AbstractAuthService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@Qualifier("keycloakAuthService")
public class KeycloakAuthService extends AbstractAuthService {

    @Value("${auth.url}")
    private String authUrl;

    @Override
    public Object login(HttpServletRequest request, HttpServletResponse response) throws AuthServerException {
        MultiValueMap<String, String> formData = this.getFormData(request);
        return this.postRequest(this.authUrl + SecurityConstants.KEYCLOAK_LOGIN_PATH, formData);
    }

    @Override
    public Object validate(HttpServletRequest request, HttpServletResponse response) throws AuthServerException {
        HttpHeaders header = new HttpHeaders();
        String auth = String.format("%s:%s", this.getClientId(request), this.getClientSecret(request));
        header.set("Authorization", "Basic " + new String(Base64.encode(auth.getBytes(StandardCharsets.UTF_8))));

        MultiValueMap<String, String> formData = this.getFormData(request);

        return this.postRequest(this.authUrl + SecurityConstants.KEYCLOAK_VALIDATION_PATH, header, formData);
    }

    @Override
    public Object logout(HttpServletRequest request, HttpServletResponse response) throws AuthServerException {
        HttpHeaders header = new HttpHeaders();
        String auth = String.format("%s:%s", this.getClientId(request), this.getClientSecret(request));
        header.set("Authorization", "Basic " + new String(Base64.encode(auth.getBytes(StandardCharsets.UTF_8))));

        MultiValueMap<String, String> formData = this.getFormData(request);

        return this.postRequest(this.authUrl + SecurityConstants.KEYCLOAK_LOGOUT_PATH, header, formData);
    }

}
