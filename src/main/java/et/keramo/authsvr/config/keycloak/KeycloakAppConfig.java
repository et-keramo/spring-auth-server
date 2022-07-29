package et.keramo.authsvr.config.keycloak;

import et.keramo.authsvr.aop.FreeIPAAop;
import et.keramo.authsvr.aop.KeycloakAop;
import et.keramo.authsvr.service.auth.AuthService;
import et.keramo.authsvr.service.auth.KeycloakAuthService;
import et.keramo.authsvr.service.client.ClientService;
import et.keramo.authsvr.service.client.KeycloakClientDto;
import et.keramo.authsvr.service.client.KeycloakClientService;
import et.keramo.authsvr.service.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static et.keramo.authsvr.constants.SecurityConst.KEYCLOAK_TYPE;

@RequiredArgsConstructor
@ConditionalOnProperty(value = "auth.type", havingValue = KEYCLOAK_TYPE)
@Configuration
public class KeycloakAppConfig {

    private final UserInfoService userInfoService;

    // AOP
    @Bean
    public KeycloakAop keycloakAop() {
        return new KeycloakAop();
    }

    @Bean
    public FreeIPAAop freeIPAAop() {
        return new FreeIPAAop();
    }

    // API Service
    @Bean
    public AuthService authService() {
        return new KeycloakAuthService();
    }

    @Bean
    public ClientService<KeycloakClientDto> clientService() {
        return new KeycloakClientService();
    }

    @Bean
    public UserService<FreeIPAUserDto> userService() {
        return new FreeIPAUserService();
    }

    @Bean
    public UserIntegratedService<FreeIPAUserDto> integratedUserService() {
        return new FreeIPAUserIntegratedService(userService(), userInfoService);
    }

}
