package et.keramo.authsvr.service.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import et.keramo.authsvr.util.JacksonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static et.keramo.authsvr.constants.SecurityConst.*;
import static et.keramo.authsvr.constants.keycloak.KeycloakConst.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class KeycloakClientDto extends ClientDto {

    public String id;
    public List<String> redirectUris;
    public List<String> webOrigins;
    public List<String> defaultClientScopes;
    public List<String> optionalClientScopes;

    public KeycloakClientDto(JsonNode jsonNode) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<String>> listType = new TypeReference<List<String>>(){};

        this.setClientId(JacksonUtil.findText(jsonNode, CLIENT_ID_KEY));

        List<String> grantTypes = new ArrayList<>();
        if (JacksonUtil.isTrue(jsonNode, AUTHORIZATION_CODE_GRANT_KEY)) {
            grantTypes.add(AUTHORIZATION_CODE_GRANT);
        }
        if (JacksonUtil.isTrue(jsonNode, IMPLICIT_GRANT_KEY)) {
            grantTypes.add(IMPLICIT_GRANT);
        }
        if (JacksonUtil.isTrue(jsonNode, PASSWORD_GRANT_KEY)) {
            grantTypes.add(PASSWORD_GRANT);
        }
        if (JacksonUtil.isTrue(jsonNode, CLIENT_CREDENTIALS_GRANT_KEY)) {
            grantTypes.add(CLIENT_CREDENTIALS_GRANT);
        }
        if (JacksonUtil.isTrue(jsonNode, ATTRIBUTES_KEY, REFRESH_TOKEN_GRANT_KEY)) {
            grantTypes.add(REFRESH_TOKEN_GRANT);
        }
        this.setGrantTypes(grantTypes.toArray(new String[0]));

        this.setAccessTokenValidity(JacksonUtil.findInt(jsonNode, ATTRIBUTES_KEY, ACCESS_TIMEOUT_KEY));
        this.setRefreshTokenValidity(JacksonUtil.findInt(jsonNode, ATTRIBUTES_KEY, REFRESH_TIMEOUT_KEY));
        this.id = JacksonUtil.findText(jsonNode, ID_KEY);
        this.redirectUris = JacksonUtil.findList(jsonNode, String.class, REDIRECT_URIS_KEY);
        this.webOrigins = JacksonUtil.findList(jsonNode, String.class, WEB_ORIGINS_KEY);
        this.defaultClientScopes = JacksonUtil.findList(jsonNode, String.class, DEFAULT_SCOPES_KEY);
        this.optionalClientScopes = JacksonUtil.findList(jsonNode, String.class, OPTIONAL_SCOPES_KEY);

        this.webOrigins = objectMapper.convertValue(jsonNode.get(WEB_ORIGINS_KEY), listType);
        this.defaultClientScopes = objectMapper.convertValue(jsonNode.get(DEFAULT_SCOPES_KEY), listType);
        this.optionalClientScopes = objectMapper.convertValue(jsonNode.get(OPTIONAL_SCOPES_KEY), listType);
    }

}
