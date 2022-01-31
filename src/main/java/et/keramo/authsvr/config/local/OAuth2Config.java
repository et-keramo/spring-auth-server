package et.keramo.authsvr.config.local;

import et.keramo.authsvr.security.local.OAuth2Manager;
import et.keramo.authsvr.security.local.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Value("${auth.type}")
    private String authType;

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2Manager oAuth2Manager;
    private final OAuth2UserService oAuth2UserService;
    private final JwtAccessTokenConverter jwtAccessTokenConverter;
    private final TokenEnhancer jwtTokenEnhancer;
    private final TokenStore tokenStore;

    /**
     * Spring Security 에 정의된 기본 토큰 서비스 구현체를 재정의
     * 토큰 발급 및 인가 여부 확인 시, 해당 서비스 구현체 활용
     *
     * @return Token Service
     */
    @Primary
    @Bean
    public DefaultTokenServices tokenServices() throws Exception {
        DefaultTokenServices tokenService = new DefaultTokenServices();
        tokenService.setAuthenticationManager(this.oAuth2Manager);
        tokenService.setTokenStore(this.tokenStore);
        tokenService.setSupportRefreshToken(true);

        return tokenService;
    }

    /**
     * 토큰 엔드포인트에 대한 보안 제약 정의
     *
     * @param security 보안 설정
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
            .allowFormAuthenticationForClients();
    }

    /**
     * 인증 서비스에 등록될 클라이언트 정의
     *
     * @param clients 클라이언트 설정
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
            .jdbc(this.dataSource)
            .passwordEncoder(this.passwordEncoder);
    }

    /**
     * 인증 방법 정의
     *
     * @param endpoints 엔드포인트 설정
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(this.jwtAccessTokenConverter, this.jwtTokenEnhancer));

        endpoints
            .authenticationManager(this.oAuth2Manager)
            .userDetailsService(this.oAuth2UserService)
            .tokenEnhancer(tokenEnhancerChain)
            .tokenStore(this.tokenStore)
            .reuseRefreshTokens(false);
    }

}