package et.keramo.authsvr.service.local;

import et.keramo.authsvr.constants.SecurityConstants;
import et.keramo.authsvr.exception.AuthServerException;
import et.keramo.authsvr.repository.rdb.local.client.Client;
import et.keramo.authsvr.repository.rdb.local.client.ClientRepository;
import et.keramo.authsvr.service.AbstractAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("localAuthService")
public class LocalAuthService extends AbstractAuthService {

    private final PasswordEncoder encoder;
    private final CheckTokenEndpoint checkTokenEndpoint;
    private final DefaultTokenServices tokenServices;
    private final ClientRepository clientRepository;

    @Override
    public Object login(HttpServletRequest request, HttpServletResponse response) throws AuthServerException {
        return this.postRequest(
                this.makePath(SecurityConstants.LOCAL_LOGIN_PATH),
                this.getRequestData(request)
        );
    }

    @Override
    public Object validate(HttpServletRequest request, HttpServletResponse response) {
        this.checkClientCredentials(request);
        return this.checkTokenEndpoint.checkToken(request.getParameter("token"));
    }

    @Override
    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        this.checkClientCredentials(request);
        return this.tokenServices.revokeToken(request.getParameter("token"));
    }

    private void checkClientCredentials(HttpServletRequest request) {
        String clientId = this.getClientId(request);
        String clientSecret = this.getClientSecret(request);

        if (clientId != null && clientSecret != null) {
            Optional<Client> optClient = this.clientRepository.findById(clientId);

            if (optClient.isPresent() && this.encoder.matches(clientSecret, optClient.get().getSecret())) {
                return;
            }
        }

        throw new BadClientCredentialsException();
    }

}
