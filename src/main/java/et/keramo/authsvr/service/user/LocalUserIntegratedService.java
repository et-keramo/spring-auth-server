package et.keramo.authsvr.service.user;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class LocalUserIntegratedService extends UserIntegratedService<LocalUserDto> {

    public LocalUserIntegratedService(UserService<LocalUserDto> userService, UserInfoService userInfoService) {
        super(userService, userInfoService);
    }

    @Override
    public List<UserInfoDto> list() throws Exception {
        return this.getUserInfoService().list();
    }

    @Override
    public UserInfoDto get(String userId) throws Exception {
        return this.getUserInfoService().get(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoDto add(UserInfoDto userInfoDto) throws Exception {
        UserInfoDto savedUserInfoDto = this.getUserInfoService().add(userInfoDto);

        LocalUserDto userDto = new LocalUserDto(savedUserInfoDto);
        this.getUserService().add(userDto);

        return savedUserInfoDto;
    }

    @Override
    public UserInfoDto update(UserInfoDto userInfoDto) throws Exception {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String userId) throws Exception {
        this.getUserInfoService().delete(userId);
        this.getUserService().delete(userId);
    }

    @Override
    public UserInfoDto confirm(String userId) throws Exception {
        UserInfoDto savedUserInfoDto = super.confirm(userId);

        LocalUserDto userDto = new LocalUserDto(savedUserInfoDto);
        this.getUserService().add(userDto);

        return savedUserInfoDto;
    }

}
