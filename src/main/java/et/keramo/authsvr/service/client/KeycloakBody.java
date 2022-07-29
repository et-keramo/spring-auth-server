package et.keramo.authsvr.service.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static et.keramo.authsvr.constants.keycloak.KeycloakConst.*;
import static et.keramo.authsvr.constants.SecurityConst.*;
import static et.keramo.authsvr.service.client.ClientDto.DEFAULT_ACCESS_EXPIRED;
import static et.keramo.authsvr.service.client.ClientDto.DEFAULT_REFRESH_EXPIRED;

public class KeycloakBody {

    // Add Client
    public Map<String, Object> make(KeycloakClientDto clientDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode body = this.baseBody();
        ObjectNode attributes = (ObjectNode) body.get(ATTRIBUTES_KEY);

        body.put(CLIENT_ID_KEY, clientDto.getClientId());
        body.set(REDIRECT_URIS_KEY, objectMapper.valueToTree(clientDto.redirectUris));
        body.set(WEB_ORIGINS_KEY, objectMapper.valueToTree(clientDto.webOrigins));
        body.set(DEFAULT_SCOPES_KEY, objectMapper.valueToTree(clientDto.defaultClientScopes));
        body.set(OPTIONAL_SCOPES_KEY, objectMapper.valueToTree(clientDto.optionalClientScopes));

        attributes.put(ACCESS_TIMEOUT_KEY, clientDto.getAccessTokenValidity() == null ? DEFAULT_ACCESS_EXPIRED : clientDto.getAccessTokenValidity());
        attributes.put(REFRESH_TIMEOUT_KEY, clientDto.getRefreshTokenValidity() == null ? DEFAULT_REFRESH_EXPIRED : clientDto.getRefreshTokenValidity());

        return objectMapper.convertValue(body, new TypeReference<Map<String,Object>>(){});
    }

    // Update Grant Types
    public Map<String, Object> make(String[] grantTypes) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode body = this.baseBody();
        ObjectNode attributes = (ObjectNode) body.get(ATTRIBUTES_KEY);

        List<String> grantTypeList = Arrays.asList(grantTypes);
        body.put(AUTHORIZATION_CODE_GRANT_KEY, grantTypeList.contains(AUTHORIZATION_CODE_GRANT));
        body.put(IMPLICIT_GRANT_KEY, grantTypeList.contains(IMPLICIT_GRANT));
        body.put(PASSWORD_GRANT_KEY, grantTypeList.contains(PASSWORD_GRANT));
        body.put(CLIENT_CREDENTIALS_GRANT_KEY, grantTypeList.contains(CLIENT_CREDENTIALS_GRANT));
        attributes.put(REFRESH_TOKEN_GRANT_KEY, grantTypeList.contains(REFRESH_TOKEN_GRANT));

        return objectMapper.convertValue(body, new TypeReference<Map<String,Object>>(){});
    }

    // Update Token Validity
    public Map<String, Object> make(Integer accessTokenValidity, Integer refreshTokenValidity) {
        ObjectNode body = this.baseBody();
        ObjectNode attributes = (ObjectNode) body.get(ATTRIBUTES_KEY);

        attributes.put(ACCESS_TIMEOUT_KEY, accessTokenValidity);

        if (refreshTokenValidity != null) {
            attributes.put(REFRESH_TIMEOUT_KEY, refreshTokenValidity);
        }

        return new ObjectMapper().convertValue(body, new TypeReference<Map<String,Object>>(){});
    }


    private ObjectNode baseBody() {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode body = objectMapper.createObjectNode();
        body.set(ATTRIBUTES_KEY, objectMapper.createObjectNode());

        return body;
    }

}
