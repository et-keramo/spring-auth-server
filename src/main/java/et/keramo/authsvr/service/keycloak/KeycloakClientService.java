package et.keramo.authsvr.service.keycloak;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import et.keramo.authsvr.constants.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Service
@Qualifier("keycloakClientService")
public class KeycloakClientService extends AbstractKeycloakService {

    @Override
    public Object list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.preAuthentication();

        return this.getRequest(
                this.makePath(SecurityConstants.KEYCLOAK_CLIENT_PATH),
                this.makeHeader()
        );
    }

    @Override
    public Object get(HttpServletRequest request, HttpServletResponse response, String clientId) throws Exception {
        this.preAuthentication();

        return this.getRequest(
                this.makePath(SecurityConstants.KEYCLOAK_CLIENT_PATH, "/" + clientId),
                this.makeHeader()
        );
    }

    @Override
    public Object add(HttpServletRequest request, HttpServletResponse response, Map<String, Object> client) throws Exception {
        this.preAuthentication();

        return this.postRequest(
                this.makePath(SecurityConstants.KEYCLOAK_CLIENT_PATH),
                this.makeHeader(),
                this.makeBody(client)
        );
    }

    @Override
    public Object update(HttpServletRequest request, HttpServletResponse response, Map<String, Object> client) throws Exception {
        this.preAuthentication();

        String clientId = client.get("clientId").toString();
        client.remove("clientId");

        return this.putRequest(
                this.makePath(SecurityConstants.KEYCLOAK_CLIENT_PATH, "/" + clientId),
                this.makeHeader(),
                this.makeBody(client)
        );
    }

    @Override
    public Object delete(HttpServletRequest request, HttpServletResponse response, String clientId) throws Exception {
        this.preAuthentication();

        return this.deleteRequest(
                this.makePath(SecurityConstants.KEYCLOAK_CLIENT_PATH, "/" + clientId),
                this.makeHeader()
        );
    }

    public Object getSecret(String clientId) throws Exception {
        this. preAuthentication();

        return this.getRequest(
                this.makePath(SecurityConstants.KEYCLOAK_CLIENT_PATH, "/" + clientId, SecurityConstants.KEYCLOAK_CLIENT_SECRET_PATH),
                this.makeHeader()
        );
    }

    public Object updateSecret(String clientId) throws Exception {
        this.preAuthentication();

        return this.putRequest(
                this.makePath(SecurityConstants.KEYCLOAK_CLIENT_PATH, "/" + clientId, SecurityConstants.KEYCLOAK_CLIENT_SECRET_PATH),
                this.makeHeader()
        );
    }

    public Object updateTime(String clientId, Integer access, Integer refresh) throws Exception {
        this.preAuthentication();

        return null;
    }

    private JsonObject makeBody(Map<String, Object> client) throws Exception {
        if (!client.containsKey("clientId")) {
            throw new Exception("올바른 클라이언트 정보를 입력해주세요.");
        }

        return (JsonObject) new Gson().toJsonTree(client);
    }

}
