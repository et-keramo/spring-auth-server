package et.keramo.authsvr.api.rest.client;

import et.keramo.authsvr.service.client.ClientService;
import et.keramo.authsvr.service.client.KeycloakClientDto;
import et.keramo.authsvr.service.client.KeycloakClientService;
import et.keramo.common.api.ApiResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static et.keramo.authsvr.constants.SecurityConst.KEYCLOAK_TYPE;

@ConditionalOnProperty(value = "auth.type", havingValue = KEYCLOAK_TYPE)
@RestController
public class KeycloakClientRestController extends ClientRestController<ClientService<KeycloakClientDto>, KeycloakClientDto> {

    private final KeycloakClientService clientService;

    @Autowired
    public KeycloakClientRestController(ClientService<KeycloakClientDto> clientService) {
        super(clientService);
        this.clientService = (KeycloakClientService) clientService;
    }

    @PutMapping("scope")
    public ApiResponseDto updateScope(
            @RequestParam(value = "client_id") String id,
            @RequestParam(value = "default_scopes") String[] defaultScopes,
            @RequestParam(value = "optional_scopes") String[] optionalScopes
    ) throws Exception {
        return new ApiResponseDto(true, this.clientService.updateScope(id, defaultScopes, optionalScopes));
    }

}
