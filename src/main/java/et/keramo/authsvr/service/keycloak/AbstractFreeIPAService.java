package et.keramo.authsvr.service.keycloak;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import et.keramo.authsvr.constants.SecurityConstants;
import et.keramo.authsvr.service.AbstractDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public abstract class AbstractFreeIPAService extends AbstractDataService {

    private static final String USER_KEY = "user";
    private static final String PASSWORD_KEY = "password";
    private static final String METHOD_KEY = "method";
    private static final String PARAMS_KEY = "params";

    @Value("${auth.free-ipa-url}")
    private String freeIpaUrl;

    @Value("${auth.free-ipa-referer}")
    private String freeIpaReferer;

    // Authentication
    private void preAuthentication() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(USER_KEY, "admin");
        body.add(PASSWORD_KEY, "admin");

        this.postRequest(this.freeIpaUrl + SecurityConstants.FREE_IPA_AUTH_PATH, body);
    }

    // Make Header & Body
    private HttpHeaders makeHeader() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.add("Referer", this.freeIpaReferer);
        return header;
    }

    private JsonObject makeBody(String method) {
        JsonObject body = new JsonObject();
        body.addProperty(METHOD_KEY, method);

        JsonArray params = new JsonArray();
        params.add(new JsonArray());
        params.add(new JsonObject());
        body.add(PARAMS_KEY, params);

        return body;
    }

    private JsonObject makeBody(String method, String username) {
        JsonObject body = this.makeBody(method);
        JsonArray params = body.getAsJsonArray(PARAMS_KEY);

        params.get(0).getAsJsonArray().add(username);

        return body;
    }

    private JsonObject makeBody(String method, Map<String, Object> user) throws Exception {
        JsonObject body = this.makeBody(method);
        JsonArray params = body.getAsJsonArray(PARAMS_KEY);

        if (!user.containsKey("username")) {
            throw new Exception("올바른 사용자 정보를 입력해주세요.");
        }

        String username = user.get("username").toString();
        user.remove("username");

        params.get(0).getAsJsonArray().add(username);
        params.set(1, new Gson().toJsonTree(user));

        return body;
    }

}
