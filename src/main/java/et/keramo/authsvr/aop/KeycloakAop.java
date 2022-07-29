package et.keramo.authsvr.aop;

import com.fasterxml.jackson.databind.JsonNode;
import et.keramo.authsvr.service.auth.KeycloakAuthService;
import et.keramo.authsvr.service.client.KeycloakClientService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

import static et.keramo.authsvr.constants.keycloak.KeycloakConst.*;

@Aspect
public class KeycloakAop {

    @Value("${auth.master-realm:master}")
    private String masterRealm;

    @Around("@annotation(et.keramo.authsvr.annotation.KeycloakAuthorization)")
    public Object setClient(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        KeycloakAuthService authService = (KeycloakAuthService) pjp.getThis();

        String clientId = authService.getClientId(request);
        String clientSecret = authService.getClientSecret(request);
        String auth = String.format("%s:%s", clientId, clientSecret);

        authService.setAuthorization("Basic " + new String(Base64.encode(auth.getBytes(StandardCharsets.UTF_8))));
        Object result = pjp.proceed();
        authService.setAuthorization(null);

        return result;
    }

    @Around("@annotation(et.keramo.authsvr.annotation.KeycloakAPI)")
    public Object preAuthentication(ProceedingJoinPoint pjp) throws Throwable {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE_PARAM, "password");
        body.add(CLIENT_ID_PARAM, "admin-cli");
        body.add(USERNAME_PARAM, "admin");
        body.add(PASSWORD_PARAM, "meta1234!@#$");

        KeycloakClientService clientService = (KeycloakClientService) pjp.getThis();

        String path = clientService.getAuthUrl() + API_PATH(this.masterRealm) + LOGIN_PATH;
        JsonNode response = clientService.postRequest(path, body);

        String token = response.get(TOKEN_KEY).asText();

        clientService.setToken("Bearer " + token);
        Object result = pjp.proceed();
        clientService.setToken(null);

        return result;
    }

}
