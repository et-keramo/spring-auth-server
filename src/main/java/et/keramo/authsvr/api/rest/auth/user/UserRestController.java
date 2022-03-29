package et.keramo.authsvr.api.rest.auth.user;

import et.keramo.authsvr.service.local.local.user.UserDto;
import et.keramo.authsvr.service.local.local.user.UserService;
import et.keramo.common.api.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final String name = "사용자";

    private final UserService service;

    @RequestMapping(value="list", method = RequestMethod.GET)
    public ApiResponseDto list() {
        try {
            return new ApiResponseDto(true, this.service.list());
        } catch(Exception e) {
            log.error(name + " 목록 조회 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public ApiResponseDto get(
            @RequestParam(value = "username") String username
    ) {
        try {
            return new ApiResponseDto(true, this.service.get(username));
        } catch(Exception e) {
            log.error(name + " 조회 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public ApiResponseDto add(@RequestBody UserDto userDto) {
        try {
            return new ApiResponseDto(true, this.service.add(userDto));
        } catch(Exception e) {
            log.error(name + " 등록 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value="", method = RequestMethod.DELETE)
    public ApiResponseDto delete(
            @RequestParam(value = "username") String username
    ) {
        try {
            return new ApiResponseDto(this.service.delete(username));
        } catch(Exception e) {
            log.error(name + " 삭제 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());

        }
    }

    @RequestMapping(value="password", method = RequestMethod.PUT)
    public ApiResponseDto updatePassword(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "origin") String origin,
            @RequestParam(value = "update") String update
    ) {
        try {
            return new ApiResponseDto(this.service.updatePassword(username, origin, update));
        } catch (Exception e) {
            log.error(name + " 비밀번호 변경 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value="roles", method = RequestMethod.PUT)
    public ApiResponseDto updateRoles(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "roles") String roles
    ) {
        try {
            return new ApiResponseDto(this.service.updateRoles(username, roles));
        } catch (Exception e) {
            log.error(name + " 역할 변경 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

}
