package et.keramo.authsvr.config.local;

import et.keramo.authsvr.security.local.JwtTokenConverter;
import et.keramo.authsvr.security.local.JwtTokenEnhancer;
import et.keramo.authsvr.security.local.OAuth2Manager;
import et.keramo.authsvr.security.local.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.Arrays;

import static et.keramo.authsvr.constants.SecurityConst.LOCAL_TYPE;

@RequiredArgsConstructor
@ConditionalOnProperty(value = "auth.type", havingValue = LOCAL_TYPE)
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;
    private final OAuth2Manager oAuth2Manager;
    private final OAuth2UserService oAuth2UserService;
//    private final JwtAccessTokenConverter jwtAccessTokenConverter;
//    private final JwtTokenEnhancer jwtTokenEnhancer;
    private final TokenStore tokenStore;

    /**
     * 토큰 엔드포인트에 대한 보안 제약 정의
     *
     * @param security 보안 설정
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
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
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(this.jwtAccessTokenConverter, this.jwtTokenEnhancer));
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new JwtTokenConverter(), new JwtTokenEnhancer()));

        endpoints
            .authenticationManager(this.oAuth2Manager)
            .userDetailsService(this.oAuth2UserService)
            .tokenEnhancer(tokenEnhancerChain)
            .tokenStore(this.tokenStore)
            .reuseRefreshTokens(false);
    }

}