package et.keramo.authsvr.security.local;

import et.keramo.authsvr.constants.SecurityConstants;
import et.keramo.authsvr.repository.rdb.auth.user.User;
import et.keramo.authsvr.repository.rdb.auth.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2Manager implements AuthenticationManager {

    private final PasswordEncoder encoder;
    private final UserRepository repository;

    /**
     * 사용자 인증 시, 사용자를 확인하고 확인된 사용자 인증 정보 반환
     *
     * @param authentication 인증 정보
     * @return 인증 정보
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<User> optUser = this.repository.findById(authentication.getName());

        if (optUser.isPresent()) {
            User user = optUser.get();

            if (this.encoder.matches((CharSequence) authentication.getCredentials(), user.getPassword())) {
                List<GrantedAuthority> authorities = new ArrayList<>();

                if (user.getRoles() != null) {
                    String rolesStr = user.getRoles().replaceAll(" ", "");

                    if (rolesStr.contains(",")) {
                        String[] roles = rolesStr.split(",");
                        for (String role : roles) {
                            authorities.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + role));
                        }
                    } else {
                        authorities.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + rolesStr));
                    }
                }

                return new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        authorities.isEmpty() ? null : authorities
                );
            }
        }

        throw new BadCredentialsException("Bad user credentials");
    }

}
