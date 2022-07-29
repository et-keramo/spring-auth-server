package et.keramo.authsvr.api.rest.user;

import et.keramo.authsvr.service.user.UserIntegratedService;
import et.keramo.authsvr.service.user.LocalUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RestController;

import static et.keramo.authsvr.constants.SecurityConst.LOCAL_TYPE;

@ConditionalOnProperty(value = "auth.type", havingValue = LOCAL_TYPE)
@RestController
public class LocalUserRestController extends UserRestController<UserIntegratedService<LocalUserDto>, LocalUserDto> {

    @Autowired
    public LocalUserRestController(UserIntegratedService<LocalUserDto> userIntegratedService) {
        super(userIntegratedService);
    }

}
