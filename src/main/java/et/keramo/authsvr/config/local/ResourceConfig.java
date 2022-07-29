package et.keramo.authsvr.config.local;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import static et.keramo.authsvr.constants.SecurityConst.LOCAL_TYPE;

@ConditionalOnProperty(value = "auth.type", havingValue = LOCAL_TYPE)
@Configuration
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {

    /**
     * 리소스 정의
     *
     * @param resources 리소스 설정
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("auth-server-resource");
    }

    /**
     *  Spring Security OAuth2 Http 규칙 정의 (REST API 관련 규칙)
     *  - WebSecurityConfigurerAdapter HttpSecurity configure 보다 우선 적용
     *
     * @param http 보안 설정
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .anonymous().disable();

        http
            .authorizeRequests()
//            .antMatchers("/api/client/**").hasRole(SecurityConstants.ROLE_SYSTEM)
//            .antMatchers("/api/user/**").hasRole(SecurityConstants.ROLE_SYSTEM)
            .antMatchers("/api/client/**").permitAll()
            .antMatchers("/api/user/**").permitAll()
            .anyRequest().authenticated();

    }

}
