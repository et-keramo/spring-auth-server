package et.keramo.authsvr.service.auth;

import et.keramo.authsvr.exception.AuthServerException;
import et.keramo.authsvr.repository.client.LocalClient;
import et.keramo.authsvr.repository.client.LocalClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static et.keramo.authsvr.constants.local.LocalConst.LOGIN_PATH;

@RequiredArgsConstructor
public class LocalAuthService extends AuthService {

    private final PasswordEncoder encoder;
    private final CheckTokenEndpoint checkTokenEndpoint;
    private final DefaultTokenServices tokenServices;
    private final LocalClientRepository clientRepository;

    @Override
    public Object login(HttpServletRequest request, HttpServletResponse response) throws AuthServerException {
        return this.postRequest(
                this.makePath(LOGIN_PATH),
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
            Optional<LocalClient> optClient = this.clientRepository.findById(clientId);

            if (optClient.isPresent() && this.encoder.matches(clientSecret, optClient.get().getSecret())) {
                return;
            }
        }

        throw new BadClientCredentialsException();
    }

}
