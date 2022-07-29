package et.keramo.authsvr.api.rest.client;

import et.keramo.authsvr.service.client.ClientService;
import et.keramo.authsvr.service.client.LocalClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RestController;

import static et.keramo.authsvr.constants.SecurityConst.LOCAL_TYPE;

@ConditionalOnProperty(value = "auth.type", havingValue = LOCAL_TYPE)
@RestController
public class LocalClientRestController extends ClientRestController<ClientService<LocalClientDto>, LocalClientDto> {

    @Autowired
    public LocalClientRestController(ClientService<LocalClientDto> clientService) {
        super(clientService);
    }

}
