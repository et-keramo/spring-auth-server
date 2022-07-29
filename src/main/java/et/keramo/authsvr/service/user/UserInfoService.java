package et.keramo.authsvr.service.user;

import et.keramo.authsvr.repository.user.UserInfo;
import et.keramo.authsvr.repository.user.UserInfoRepository;
import et.keramo.authsvr.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserInfoService implements ApiService<UserInfoDto, String> {

    private final UserInfoRepository repository;

    @Override
    public List<UserInfoDto> list() {
        return this.repository.findAll().stream().map(UserInfoDto::new).collect(Collectors.toList());
    }

    @Override
    public UserInfoDto get(String userId) {
        return this.repository.findById(userId).map(UserInfoDto::new).orElse(null);
    }

    @Override
    public UserInfoDto add(UserInfoDto userInfoDto) throws Exception {
        boolean isExist = this.repository.findById(userInfoDto.getUserId()).isPresent();
        if (isExist) {
            throw new Exception("등록 대기중 또는 등록된 사용자입니다.");
        }

        UserInfo savedUserInfo = this.repository.saveAndFlush(userInfoDto.toEntity());

        return new UserInfoDto(savedUserInfo);
    }

    @Override
    public UserInfoDto update(UserInfoDto userInfoDto) throws Exception {
        return null;
    }

    @Override
    public void delete(String userId) {
        this.repository.deleteById(userId);
    }

    public UserInfoDto confirm(String userId) throws Exception {
        UserInfo userInfo = this.checkUserInfo(userId);

        UserInfoDto userInfoDto = new UserInfoDto(userInfo);
        userInfoDto.setConfirmDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        UserInfo savedUserInfo = this.repository.saveAndFlush(userInfoDto.toEntity());

        return new UserInfoDto(savedUserInfo);
    }


    private UserInfo checkUserInfo(String userId) throws Exception {
        return this.repository.findById(userId).orElseThrow(() -> new Exception("존재하지 않는 사용자입니다."));
    }

}
