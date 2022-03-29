package et.keramo.authsvr.config;

import com.google.common.collect.ImmutableList;
import et.keramo.authsvr.constants.SecurityConstants;
import et.keramo.authsvr.service.local.keycloak.KeycloakAuthService;
import et.keramo.authsvr.service.local.local.LocalAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${auth.type}")
    private String authType;

    private final ApplicationContext context;

    /**
     * 인증 서비스 정의
     *
     * @return 인증 서비스
     */
    @Bean
    public AbstractAuthService authService() {
        if (this.authType.equals(SecurityConstants.KEYCLOAK_AUTH_TYPE)) {
            return this.context.getBean(KeycloakAuthService.class);
        } else {
            return this.context.getBean(LocalAuthService.class);
        }
    }

    /**
     * Password Encoder 정의
     *
     * @return Password Encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder delegatingEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        delegatingEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        return delegatingEncoder;
    }

    /**
     * Cors 설정 정의
     *
     * @return Cors 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("*"));
        configuration.setExposedHeaders(ImmutableList.of("X-Auth-Token", "Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Spring Security Http 기본 규칙 정의
     * - ResourceServerConfigurerAdapter HttpSecurity configure 보다 이후 적용
     *
     * @param http 보안 설정
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }

    /**
     * Spring Security Http 기본 규칙 예외 정의
     *
     * @param web 보안 예외 설정
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/oauth/login", "/oauth/validation", "/oauth/logout");
    }

}
