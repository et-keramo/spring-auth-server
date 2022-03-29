package et.keramo.authsvr.service.user;

import et.keramo.authsvr.repository.rdb.local.user.User;
import et.keramo.authsvr.repository.rdb.local.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("localUserService")
public class LocalUserService {

    private final PasswordEncoder encoder;
    private final UserRepository repository;

    public List<UserDto> list() {
        return this.repository.findAll().stream().map(UserDto::new).collect(Collectors.toList());
    }

    public UserDto get(String username) {
        return this.repository.findById(username).map(UserDto::new).orElse(null);
    }

    public UserDto add(UserDto userDto) throws Exception {
        if (userDto.getUsername() == null || userDto.getPassword() == null) {
            throw new Exception("올바른 사용자 정보를 입력해주세요.");
        }

        boolean isExist = this.repository.findById(userDto.getUsername()).isPresent();
        if (isExist) {
            throw new Exception("이미 존재하는 사용자입니다.");
        }

        userDto.setPassword(this.encoder.encode(userDto.getPassword()));

        User savedUser = this.repository.saveAndFlush(userDto.toEntity());

        return new UserDto(savedUser);
    }

    public boolean delete(String username) {
        this.repository.deleteById(username);
        return true;
    }

    public boolean updatePassword(String username, String origin, String update) throws Exception {
        User user = this.checkUser(username);

        if (this.encoder.matches(origin, user.getPassword())) {
            UserDto userDto = new UserDto(user);
            userDto.setPassword(this.encoder.encode(update));

            this.repository.saveAndFlush(userDto.toEntity());

            return true;
        }

        throw new Exception("비밀번호가 올바르지 않습니다.");
    }

    public boolean updateRoles(String username, String roles) throws Exception {
        User user = this.checkUser(username);

        UserDto userDto = new UserDto(user);
        userDto.setPassword(user.getPassword());
        userDto.setRoles(roles);

        this.repository.saveAndFlush(userDto.toEntity());

        return true;
    }

    private User checkUser(String username) throws Exception {
        User user = this.repository.findById(username).orElse(null);

        if (user == null) {
            throw new Exception("존재하지 않는 사용자입니다.");
        }
        return user;
    }

}
