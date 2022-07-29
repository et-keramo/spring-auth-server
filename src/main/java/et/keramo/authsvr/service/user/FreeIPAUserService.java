package et.keramo.authsvr.service.user;

import com.fasterxml.jackson.databind.JsonNode;
import et.keramo.authsvr.annotation.FreeIPAAPI;
import et.keramo.authsvr.repository.user.UserInfoRepository;
import et.keramo.authsvr.service.ExternalRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

import static et.keramo.authsvr.constants.keycloak.FreeIPAConst.*;
import static io.jsonwebtoken.lang.Assert.notNull;

@RequiredArgsConstructor
public class FreeIPAUserService extends ExternalRestService implements UserService<FreeIPAUserDto> {

    @Value("${auth.free-ipa-url:https://127.0.0.1/ipa/session}")
    private String freeIpaUrl;

    @Value("${auth.free-ipa-referer:https://127.0.0.1/ipa}")
    private String freeIpaReferer;

    @Override
    public String getBasePath() {
        return this.freeIpaUrl;
    }

    @Override
    public HttpHeaders makeHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Referer", this.freeIpaReferer);
        return header;
    }

    @FreeIPAAPI
    @Override
    public List<FreeIPAUserDto> list() throws Exception {
        FreeIPARequestBody body = new FreeIPARequestBody(USER_LIST_METHOD);
        JsonNode response = this.request(body);
        JsonNode result = this.getResult(response);

        List<FreeIPAUserDto> userDtoList = new ArrayList<>();
        result.forEach(o -> userDtoList.add(new FreeIPAUserDto(o)));
        return userDtoList;
    }

    @FreeIPAAPI
    @Override
    public FreeIPAUserDto get(String userId) throws Exception {
        FreeIPARequestBody body = new FreeIPARequestBody(USER_GET_METHOD).userId(userId);
        JsonNode response = this.request(body);
        JsonNode result = this.getResult(response);

        return new FreeIPAUserDto(result);
    }

    @FreeIPAAPI
    @Override
    public FreeIPAUserDto add(FreeIPAUserDto userDto) throws Exception {
        FreeIPARequestBody body = new FreeIPARequestBody(USER_ADD_METHOD).user(userDto);
        JsonNode response = this.request(body);
        JsonNode result = this.getResult(response);

        return new FreeIPAUserDto(result);
    }

    @Override
    public FreeIPAUserDto update(FreeIPAUserDto userDto) throws Exception {
        return null;
    }

    @FreeIPAAPI
    @Override
    public void delete(String userId) throws Exception {
        FreeIPARequestBody body = new FreeIPARequestBody(USER_DELETE_METHOD).userId(userId);
        JsonNode response = this.request(body);

        this.getResult(response);
    }

    @FreeIPAAPI
    @Override
    public void updatePassword(String userId, String origin, String update) throws Exception {
        FreeIPARequestBody body = new FreeIPARequestBody(PASSWD_METHOD).password(userId, origin, update);
        JsonNode response = this.request(body);

        this.getResult(response);
    }


    // FreeIPA API 요청
    private JsonNode request(Object body) {
        String path = this.makePath(JSON_RPC_PATH);
        return this.postRequest(path, body);
    }

    /**
     * Free IPA API 응답 결과 추출
     * - Example)
     * {
     *      "result": {
     *          "result": {} OR []
     *      },
     *      "error": {
     *          "message": ...,
     *          "code": ...
     *          "data": ...
     *      }
     * }
     */
    private JsonNode getResult(JsonNode jsonNode) throws Exception {
        if (jsonNode.hasNonNull("result")) {
            return jsonNode.get("result").get("result");
        } else if (jsonNode.hasNonNull("error")) {
            String message = jsonNode.get("error").get("message").asText();
            throw new Exception(message);
        } else {
            throw new Exception("Unable to read response");
        }
    }

}
