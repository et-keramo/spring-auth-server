package et.keramo.authsvr.api.rest.user;

import et.keramo.authsvr.api.rest.ApiController;
import et.keramo.authsvr.service.user.*;
import et.keramo.common.api.ApiResponseDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 사용자 API 공통
 *
 * @param <S> 사용자 통합 서비스 타입
 * @param <T> 사용자 DTO 타입
 */
@RequestMapping("/api/user")
public abstract class UserRestController<S extends UserIntegratedService<T>, T extends UserDto> extends ApiController<UserInfoDto, String> {

    private final S service;

    public UserRestController(S service) {
        super(service);
        this.service = service;
    }

    // Common Service
    @Override
    public ApiResponseDto get(@RequestParam(value = "user_id") String userId) throws Exception {
        return super.get(userId);
    }

    @Override
    public ApiResponseDto add(@Valid @RequestBody UserInfoDto userInfoDto) throws Exception {
        ApiResponseDto apiResponseDto = super.add(userInfoDto);
        apiResponseDto.setMessage("등록 요청");
        return apiResponseDto;
    }

    @Override
    public ApiResponseDto update(@RequestBody UserInfoDto userInfoDto) throws Exception {
        throw new Exception("Not Support API");
    }

    @Override
    public ApiResponseDto delete(@RequestParam(value = "user_id") String userId) throws Exception {
        return super.delete(userId);
    }

    // User Service
    @PutMapping("password")
    public ApiResponseDto updatePassword(
            @RequestParam(value = "user_id") String userId,
            @RequestParam(value = "origin", required = false) String origin,
            @RequestParam(value = "update") String update
    ) throws Exception {
        this.service.getUserService().updatePassword(userId, origin, update);
        return new ApiResponseDto(true,"비밀번호 변경 성공");
    }

    // Admin Service
    @PostMapping("register")
    public ApiResponseDto register(@RequestBody UserInfoDto userInfoDto) throws Exception {
        return new ApiResponseDto(true, this.service.register(userInfoDto), "등록 성공");
    }

    @PostMapping("confirm")
    public ApiResponseDto confirm(@RequestParam(value = "user_id") String userId) throws Exception {
        return new ApiResponseDto(true, this.service.confirm(userId), "등록 승인");
    }

    @PostMapping("reject")
    public ApiResponseDto reject(@RequestParam(value = "user_id") String userId) throws Exception {
        this.service.reject(userId);
        return new ApiResponseDto(true, "등록 거부");
    }

}
