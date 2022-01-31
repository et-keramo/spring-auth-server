package et.keramo.authsvr.security.local;

import et.keramo.authsvr.repository.rdb.auth.user.User;
import et.keramo.authsvr.repository.rdb.auth.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserService implements UserDetailsService {

    private final UserRepository repository;

    /**
     * Refresh Token 호출 시, 사용자를 확인하고 확인된 사용자 정보 반환
     *
     * @param username 사용자 ID
     * @return 사용자 정보
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<User> optUser = repository.findById(username);

            if (optUser.isPresent()) {
                User user = optUser.get();
                UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword());
                return user.getRoles() == null ? builder.roles("ROLE").build() : builder.roles(user.getRoles()).build();
            }

            log.error("Not found user: {}", username);
            throw new UsernameNotFoundException("User not found.");
    }

}
