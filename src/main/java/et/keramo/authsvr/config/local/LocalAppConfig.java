package et.keramo.authsvr.config.local;

import et.keramo.authsvr.repository.client.LocalClientRepository;
import et.keramo.authsvr.repository.user.LocalUserRepository;
import et.keramo.authsvr.security.local.OAuth2Manager;
import et.keramo.authsvr.security.local.OAuth2UserService;
import et.keramo.authsvr.service.auth.AuthService;
import et.keramo.authsvr.service.auth.LocalAuthService;
import et.keramo.authsvr.service.client.ClientService;
import et.keramo.authsvr.service.client.LocalClientDto;
import et.keramo.authsvr.service.client.LocalClientService;
import et.keramo.authsvr.service.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

import static et.keramo.authsvr.constants.SecurityConst.LOCAL_TYPE;

@RequiredArgsConstructor
@ConditionalOnProperty(value = "auth.type", havingValue = LOCAL_TYPE)
@Configuration
public class LocalAppConfig {

    private final PasswordEncoder encoder;
    private final DataSource dataSource;
    private final CheckTokenEndpoint checkTokenEndpoint;

    private final UserInfoService userInfoService;

    private final LocalClientRepository clientRepository;
    private final LocalUserRepository userRepository;

    /**
     * Spring Security 에 정의된 기본 토큰 서비스 구현체를 재정의
     * 토큰 발급 및 인가 여부 확인 시, 해당 서비스 구현체 활용
     *
     * @return Token Service
     */
    @Primary
    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenService = new DefaultTokenServices();
        tokenService.setAuthenticationManager(oAuth2Manager());
        tokenService.setTokenStore(tokenStore());
        tokenService.setSupportRefreshToken(true);

        return tokenService;
    }

    /**
     * 토큰 저장소 정의
     *
     * @return 토큰 저장소
     */
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(this.dataSource);
    }

    /**
     * 액세스 토큰을 JWT 토큰으로 변환
     *
     * @return JWT Converter(Enhancer)
     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        return new JwtTokenConverter();
//    }

    /**
     * 액세스 토큰에 원하는 정보를 추가
     *
     * @return JWT Enhancer
     */
//    @Bean
//    public TokenEnhancer jwtTokenEnhancer() {
//        return new JwtTokenEnhancer();
//    }

    // OAuth 2.0
    @Bean
    public OAuth2Manager oAuth2Manager() {
        return new OAuth2Manager(encoder, userRepository);
    }

    @Bean
    public OAuth2UserService oAuth2UserService() {
        return new OAuth2UserService(userRepository);
    }


    // API Service
    @Bean
    public AuthService authService() {
        return new LocalAuthService(encoder, checkTokenEndpoint, tokenServices(), clientRepository);
    }

    @Bean
    public ClientService<LocalClientDto> clientService() {
        return new LocalClientService(encoder, tokenStore(), clientRepository);
    }

    @Bean
    public UserService<LocalUserDto> userService() {
        return new LocalUserService(encoder, userRepository);
    }

    @Bean
    public UserIntegratedService<LocalUserDto> integratedUserService() {
        return new LocalUserIntegratedService(userService(), userInfoService);
    }

}
