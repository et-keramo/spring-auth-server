package et.keramo.authsvr.api.rest.auth;

import et.keramo.authsvr.service.auth.AuthService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RestController;

import static et.keramo.authsvr.constants.SecurityConst.LOCAL_TYPE;

@ConditionalOnProperty(value = "auth.type", havingValue = LOCAL_TYPE)
@RestController
public class LocalAuthRestController extends AuthRestController {

    public LocalAuthRestController(AuthService authService) {
        super(authService);
    }

}
