package et.keramo.authsvr.service.user;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class FreeIPAUserIntegratedService extends UserIntegratedService<FreeIPAUserDto> {

    public FreeIPAUserIntegratedService(UserService<FreeIPAUserDto> userService, UserInfoService userInfoService) {
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

    @Override
    public UserInfoDto add(UserInfoDto userInfoDto) throws Exception {
        return  this.getUserInfoService().add(userInfoDto);
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoDto register(UserInfoDto userInfoDto) throws Exception {
        UserInfoDto savedUserInfoDto = super.register(userInfoDto);

        FreeIPAUserDto userDto = new FreeIPAUserDto(savedUserInfoDto);
        this.getUserService().add(userDto);

        return savedUserInfoDto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoDto confirm(String userId) throws Exception {
        UserInfoDto savedUserInfoDto = super.confirm(userId);

        FreeIPAUserDto userDto = new FreeIPAUserDto(savedUserInfoDto);
        this.getUserService().add(userDto);

        return savedUserInfoDto;
    }

}
