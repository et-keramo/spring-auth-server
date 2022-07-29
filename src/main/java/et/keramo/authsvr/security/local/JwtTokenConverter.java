package et.keramo.authsvr.security.local;

import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import static et.keramo.authsvr.constants.local.LocalConst.JWT_SIGN_KEY;

public class JwtTokenConverter extends JwtAccessTokenConverter {

    /**
     * 액세스 토큰을 JWT 토큰으로 변환
     */
   public JwtTokenConverter() {
       // 대칭키
       this.setSigningKey(JWT_SIGN_KEY);

       // 비대칭키
//        KeyStoreKeyFactory keyStoreKeyFactory =  new KeyStoreKeyFactory(new ClassPathResource("oauth2.jks"), "PASSWORD".toCharArray());
//        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("oauth2", "PASSWORD".toCharArray());
//        this.setKeyPair(keyPair);
   }

}
