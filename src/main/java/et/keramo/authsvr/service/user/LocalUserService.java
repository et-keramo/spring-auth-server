package et.keramo.authsvr.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import et.keramo.authsvr.repository.user.LocalUser;
import et.keramo.authsvr.repository.user.LocalUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LocalUserService implements UserService<LocalUserDto> {

    private final PasswordEncoder encoder;
    private final LocalUserRepository repository;

    @Override
    public List<LocalUserDto> list() {
        return this.repository.findAll().stream().map(LocalUserDto::new).collect(Collectors.toList());
    }

    @Override
    public LocalUserDto get(String userId) {
        return this.repository.findById(userId).map(LocalUserDto::new).orElse(null);
    }

    @Override
    public LocalUserDto add(LocalUserDto user) throws Exception {
        LocalUserDto userDto = new ObjectMapper().convertValue(user, LocalUserDto.class);

        boolean isExist = this.repository.findById(userDto.getUserId()).isPresent();
        if (isExist) {
            throw new Exception("이미 존재하는 사용자입니다.");
        }

        userDto.setPassword(this.encoder.encode(userDto.getPassword()));

        LocalUser savedUser = this.repository.saveAndFlush(userDto.toEntity());

        return new LocalUserDto(savedUser);
    }

    @Override
    public LocalUserDto update(LocalUserDto object) throws Exception {
        return null;
    }

    @Override
    public void delete(String userId) {
        this.repository.deleteById(userId);
    }

    @Override
    public void updatePassword(String userId, String origin, String update) throws Exception {
        LocalUser user = this.checkUser(userId);

        if (this.encoder.matches(origin, user.getPassword())) {
            LocalUserDto userDto = new LocalUserDto(user);
            userDto.setPassword(this.encoder.encode(update));

            this.repository.saveAndFlush(userDto.toEntity());
        }

        throw new Exception("비밀번호가 올바르지 않습니다.");
    }

    public LocalUserDto updateRoles(String userId, String roles) throws Exception {
        LocalUser user = this.checkUser(userId);

        LocalUserDto userDto = new LocalUserDto(user);
        userDto.setPassword(user.getPassword());
        userDto.setRoles(roles);

        LocalUser savedUser = this.repository.saveAndFlush(userDto.toEntity());

        return new LocalUserDto(savedUser);
    }


    private LocalUser checkUser(String userId) throws Exception {
        return this.repository.findById(userId).orElseThrow(() -> new Exception("존재하지 않는 사용자입니다."));
    }

}
