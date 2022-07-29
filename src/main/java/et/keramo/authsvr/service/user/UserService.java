package et.keramo.authsvr.service.user;

import et.keramo.authsvr.service.ApiService;

/**
 * 사용자 서비스 공통
 *
 * @param <T> 사용자 DTO 타입
 */
public interface UserService<T extends UserDto> extends ApiService<T, String>  {

    void updatePassword(String userId, String origin, String update) throws Exception;

}
