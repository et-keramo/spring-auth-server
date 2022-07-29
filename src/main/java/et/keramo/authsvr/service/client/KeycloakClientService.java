package et.keramo.authsvr.service.client;

import com.fasterxml.jackson.databind.JsonNode;
import et.keramo.authsvr.annotation.KeycloakAPI;
import et.keramo.authsvr.service.ExternalRestService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import java.util.*;
import java.util.stream.Collectors;

import static et.keramo.authsvr.constants.keycloak.KeycloakConst.*;
import static org.springframework.util.Assert.isTrue;

public class KeycloakClientService extends ExternalRestService implements ClientService<KeycloakClientDto> {

    @Value("${auth.realm:default}")
    private String realm;

    @Setter
    private String token;

    private KeycloakBody keycloakBody = new KeycloakBody();

    @Override
    public String getBasePath() {
        return this.getAuthUrl() + ADMIN_API_PATH(this.realm);
    }

    @Override
    public HttpHeaders makeHeader() {
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", this.token);
        return header;
    }

    @KeycloakAPI
    @Override
    public List<KeycloakClientDto> list() throws Exception {
        String path = this.makePath(CLIENT_PATH);
        JsonNode response = this.getRequest(path);

        List<KeycloakClientDto> clientDtoList = new ArrayList<>();

        for (JsonNode o : response) {
            clientDtoList.add(new KeycloakClientDto(o));
        }
        return clientDtoList;
    }

    @KeycloakAPI
    @Override
    public KeycloakClientDto get(String id) throws Exception {
//        isTrue(StringUtils.isNotEmpty(id), "클라이언트 ID를 입력해주세요.");

        String path = this.makePath(CLIENT_PATH(id));
        JsonNode response = this.getRequest(path);

        return new KeycloakClientDto(response);
    }

    @KeycloakAPI
    @Override
    public KeycloakClientDto add(KeycloakClientDto clientDto) {
        String path = this.makePath(CLIENT_PATH);
        Map<String, Object> body = keycloakBody.make(clientDto);
        JsonNode response = this.postRequest(path, body);

        return clientDto;
    }

    @KeycloakAPI
    @Override
    public KeycloakClientDto update(KeycloakClientDto clientDto) {
        return null;
    }

    @KeycloakAPI
    @Override
    public KeycloakClientDto updateSecret(String id) throws Exception {
        String path = this.makePath(CLIENT_PATH(id), CLIENT_SECRET_PATH);
        JsonNode response = this.postRequest(path);

        KeycloakClientDto clientDto = this.get(id);
        clientDto.setSecret(response.get("value").asText());

        return clientDto;
    }

    @KeycloakAPI
    @Override
    public KeycloakClientDto updateGrantTypes(String id, String[] grantTypes) throws Exception {
        String path = this.makePath(CLIENT_PATH(id));
        Map<String, Object> body = keycloakBody.make(grantTypes);
        JsonNode response = this.putRequest(path, body);

        return this.get(id);
    }

    @KeycloakAPI
    @Override
    public KeycloakClientDto updateValidity(String id, Integer access, Integer refresh) throws Exception {
        String path = this.makePath(CLIENT_PATH(id));
        Map<String, Object> body = keycloakBody.make(access, refresh);
        JsonNode response = this.putRequest(path, body);

        return this.get(id);
    }

    @KeycloakAPI
    @Override
    public void delete(String id) {
        String path = this.makePath(CLIENT_PATH(id));
        JsonNode response = this.deleteRequest(path);
    }

    @KeycloakAPI
    public KeycloakClientDto updateScope(String id, String[] defaultScopes, String[] optionalScopes) throws Exception {
        String path = this.makePath(CLIENT_SCOPE_PATH);
        JsonNode response = this.getRequest(path);

        Map<String, String> scopeMap = new HashMap<>();
        for (JsonNode o : response) {
            scopeMap.put(o.get("name").asText(), o.get("id").asText());
        }

        List<String> list = Arrays.stream(defaultScopes)
                .filter(s -> Arrays.asList(optionalScopes).contains(s))
                .collect(Collectors.toList());

        if (list.size() > 0) {
            throw new Exception("");
        }

        for (String scope : defaultScopes) {

        }

        for (String scope : optionalScopes) {

        }

        return this.get(id);
    }

}
