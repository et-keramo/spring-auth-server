package et.keramo.authsvr.config.local;

import et.keramo.authsvr.constants.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class JwtConfig {

    private final DataSource dataSource;

    /**
     * 액세스 토큰을 JWT 토큰으로 변환
     *
     * @return JWT Converter(Enhancer)
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() throws Exception {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        // 대칭키
        converter.setSigningKey(SecurityConstants.JWT_SIGN_KEY);

        // 비대칭키
//        KeyStoreKeyFactory keyStoreKeyFactory =  new KeyStoreKeyFactory(new ClassPathResource("oauth2.jks"), "PASSWORD".toCharArray());
//        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("oauth2", "PASSWORD".toCharArray());
//        converter.setKeyPair(keyPair);

        return converter;
    }

    /**
     * 액세스 토큰에 원하는 정보를 추가
     *
     * @return JWT Enhancer
     */
    @Bean
    public TokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }

    /**
     * 토큰 저장소 정의
     *
     * @return 토큰 저장소
     */
    @Bean
    public TokenStore tokenStore() throws Exception {
        // JDBC : oauth_access_token, oauth_refresh_token
        return new JdbcTokenStore(this.dataSource);
    }

}
