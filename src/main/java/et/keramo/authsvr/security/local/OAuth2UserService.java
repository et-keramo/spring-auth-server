package et.keramo.authsvr.security.local;

import et.keramo.authsvr.repository.user.LocalUser;
import et.keramo.authsvr.repository.user.LocalUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class OAuth2UserService implements UserDetailsService {

    private final LocalUserRepository repository;

    /**
     * Refresh Token 호출 시, 사용자를 확인하고 확인된 사용자 정보 반환
     *
     * @param userId 사용자 ID
     * @return 사용자 정보
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
            Optional<LocalUser> optUser = repository.findById(userId);

            if (optUser.isPresent()) {
                LocalUser user = optUser.get();
                UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getUserId()).password(user.getPassword());
                return user.getRoles() == null ? builder.roles("ROLE").build() : builder.roles(user.getRoles()).build();
            }

            log.error("Not found user: {}", userId);
            throw new UsernameNotFoundException("User not found.");
    }

}
