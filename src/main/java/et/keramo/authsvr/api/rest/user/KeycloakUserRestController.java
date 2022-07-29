package et.keramo.authsvr.api.rest.user;

import et.keramo.authsvr.service.user.UserIntegratedService;
import et.keramo.authsvr.service.user.FreeIPAUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import static et.keramo.authsvr.constants.SecurityConst.KEYCLOAK_TYPE;

@ConditionalOnProperty(value = "auth.type", havingValue = KEYCLOAK_TYPE)
@RestController
public class KeycloakUserRestController extends UserRestController<UserIntegratedService<FreeIPAUserDto>, FreeIPAUserDto> {

    @Autowired
    public KeycloakUserRestController(UserIntegratedService<FreeIPAUserDto> userIntegratedService) {
        super(userIntegratedService);
    }

}
