package et.keramo.authsvr.service.client;

import et.keramo.authsvr.repository.client.LocalClient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class LocalClientDto extends ClientDto {

    private static final String DEFAULT_SCOPE = "read,write";

    // Types : authorization_code, implicit, password, client_credentials, refresh_token
    private static final String DEFAULT_GRANT_TYPES = "password,refresh_token";

    public String resourceIds;
    public String scope;
    public String webServerRedirectUri;
    public String authorities;
    public String additionalInformation;
    public String autoapprove;

    public LocalClientDto(LocalClient entity) {
        this.setClientId(entity.getClientId());
        this.setGrantTypes((entity.getAuthorizedGrantTypes().split(",")));
        this.setAccessTokenValidity(entity.getAccessTokenValidity());
        this.setRefreshTokenValidity(entity.getRefreshTokenValidity());
        this.resourceIds = entity.getResourceIds();
        this.scope = entity.getScope();
        this.webServerRedirectUri = entity.getWebServerRedirectUri();
        this.authorities = entity.getAuthorities();
        this.additionalInformation = entity.getAdditionalInformation();
        this.autoapprove = entity.getAutoapprove();
        this.additionalInformation = entity.getAdditionalInformation();
        this.autoapprove = entity.getAutoapprove();
    }

    public LocalClient toEntity() {
        return LocalClient.builder()
                .clientId(this.getClientId())
                .secret(this.getSecret())
                .authorizedGrantTypes(this.getGrantTypes() == null ? DEFAULT_GRANT_TYPES : String.join(",", this.getGrantTypes()))
                .accessTokenValidity(this.getAccessTokenValidity() == null ? DEFAULT_ACCESS_EXPIRED : this.getAccessTokenValidity())
                .refreshTokenValidity(this.getRefreshTokenValidity() == null ? DEFAULT_REFRESH_EXPIRED : this.getRefreshTokenValidity())
                .resourceIds(this.resourceIds)
                .scope(this.scope == null ? DEFAULT_SCOPE : this.scope)
                .webServerRedirectUri(this.webServerRedirectUri)
                .authorities(this.authorities)
                .additionalInformation(this.additionalInformation)
                .autoapprove(this.autoapprove)
                .build();
    }

}
