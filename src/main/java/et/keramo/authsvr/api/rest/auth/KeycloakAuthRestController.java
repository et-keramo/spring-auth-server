package et.keramo.authsvr.api.rest.auth;

import et.keramo.authsvr.service.auth.AuthService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import static et.keramo.authsvr.constants.SecurityConst.KEYCLOAK_TYPE;

@ConditionalOnProperty(value = "auth.type", havingValue = KEYCLOAK_TYPE)
@RestController
public class KeycloakAuthRestController extends AuthRestController {

    public KeycloakAuthRestController(AuthService authService) {
        super(authService);
    }

}
