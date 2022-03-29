package et.keramo.authsvr.service.client;

import et.keramo.authsvr.repository.rdb.local.client.Client;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ClientDto {

    private static final String DEFAULT_SCOPE = "read,write";
    private static final String DEFAULT_GRANT_TYPES = "password,refresh_token";
    private static final Integer DEFAULT_ACCESS_EXPIRED = 3600;
    private static final Integer DEFAULT_REFRESH_EXPIRED = 10800;

    private String clientId;
    private String secret;
    private String resourceIds;
    private String scope;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoapprove;

    public ClientDto(Client entity) {
        this.clientId = entity.getClientId();
        this.resourceIds = entity.getResourceIds();
        this.scope = entity.getScope();
        this.authorizedGrantTypes = entity.getAuthorizedGrantTypes();
        this.webServerRedirectUri = entity.getWebServerRedirectUri();
        this.authorities = entity.getAuthorities();
        this.accessTokenValidity = entity.getAccessTokenValidity();
        this.refreshTokenValidity = entity.getRefreshTokenValidity();
        this.additionalInformation = entity.getAdditionalInformation();
        this.autoapprove = entity.getAutoapprove();
    }

    public Client toEntity() {
        return Client.builder()
                .clientId(this.clientId)
                .secret(this.secret)
                .resourceIds(this.resourceIds)
                .scope(this.scope == null ? DEFAULT_SCOPE : this.scope)
                .authorizedGrantTypes(this.authorizedGrantTypes == null ? DEFAULT_GRANT_TYPES : this.authorizedGrantTypes)
                .webServerRedirectUri(this.webServerRedirectUri)
                .authorities(this.authorities)
                .accessTokenValidity(this.accessTokenValidity == null ? DEFAULT_ACCESS_EXPIRED : this.accessTokenValidity)
                .refreshTokenValidity(this.refreshTokenValidity == null ? DEFAULT_REFRESH_EXPIRED : this.refreshTokenValidity)
                .additionalInformation(this.additionalInformation)
                .autoapprove(this.autoapprove)
                .build();
    }

}
