package et.keramo.authsvr.api.rest.auth.client;

import et.keramo.authsvr.service.local.local.client.ClientDto;
import et.keramo.authsvr.service.local.local.client.ClientService;
import et.keramo.common.api.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/client")
public class ClientRestController {

    private final String name = "클라이언트";

    private final ClientService service;

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
    public ApiResponseDto get(@RequestParam(value = "client_id") String id) {
        try {
            return new ApiResponseDto(true, this.service.get(id));
        } catch(Exception e) {
            log.error(name + " 조회 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public ApiResponseDto add(@RequestBody ClientDto clientDto) {
        try {
            return new ApiResponseDto(true, this.service.add(clientDto));
        } catch(Exception e) {
            log.error(name + " 등록 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value="", method = RequestMethod.DELETE)
    public ApiResponseDto delete(@RequestParam(value = "client_id") String id) {
        try {
            return new ApiResponseDto(this.service.delete(id));
        } catch(Exception e) {
            log.error(name + " 삭제 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value="regenerateSecret", method = RequestMethod.PUT)
    public ApiResponseDto regenerateSecret(
            @RequestParam(value = "client_id") String id,
            @RequestParam(value = "client_secret") String secret
    ) {
        try {
            return new ApiResponseDto(true, this.service.regenerateSecret(id, secret));
        } catch (Exception e) {
            log.error(name + " 시크릿 키 재생성 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value="scope", method = RequestMethod.PUT)
    public ApiResponseDto updateScope(
            @RequestParam(value = "client_id") String id,
            @RequestParam(value = "scope") String scope
    ) {
        try {
            return new ApiResponseDto(this.service.updateScope(id, scope));
        } catch (Exception e) {
            log.error(name + " 범위 수정 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value="grant_types", method = RequestMethod.PUT)
    public ApiResponseDto updateGrantTypes(
            @RequestParam(value = "client_id") String id,
            @RequestParam(value = "grant_types") String grantTypes
    ) {
        try {
            return new ApiResponseDto(this.service.updateGrantTypes(id, grantTypes));
        } catch (Exception e) {
            log.error(name + " 인증 유형 수정 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value="validity", method = RequestMethod.PUT)
    public ApiResponseDto updateValidity(
            @RequestParam(value = "client_id") String id,
            @RequestParam(value = "access") Integer access,
            @RequestParam(value = "refresh", required = false) Integer refresh
    ) {
        try {
            return new ApiResponseDto(this.service.updateValidity(id, access, refresh));
        } catch (Exception e) {
            log.error(name + " 만료 시간 수정 중 오류 발생", e);
            return new ApiResponseDto(false, e.getMessage());
        }
    }

}
