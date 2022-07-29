package et.keramo.authsvr.service.user;

import et.keramo.authsvr.service.ApiService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 통합 사용자 서비스 공통
 *
 * @param <T> 사용자 DTO 타입
 */
@RequiredArgsConstructor
@Getter
public abstract class UserIntegratedService<T extends UserDto> implements ApiService<UserInfoDto, String> {

    private final UserService<T> userService;
    private final UserInfoService userInfoService;

    public UserInfoDto register(UserInfoDto userInfoDto) throws Exception {
        return this.userInfoService.add(userInfoDto);
    }

    public UserInfoDto confirm(String userId) throws Exception {
        return this.userInfoService.confirm(userId);
    }

    public void reject(String userId) throws Exception {
        this.userInfoService.delete(userId);
    }

}
