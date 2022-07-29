package et.keramo.authsvr.api.rest.client;

import et.keramo.authsvr.api.rest.ApiController;
import et.keramo.authsvr.service.client.ClientDto;
import et.keramo.authsvr.service.client.ClientService;
import et.keramo.common.api.ApiResponseDto;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 클라이언트 API 공통
 *
 * @param <S> 클라이언트 서비스 타입
 * @param <T> 클라이언트 DTO 타입
 */
@RequestMapping("/api/client")
public abstract class ClientRestController<S extends ClientService<T>, T extends ClientDto> extends ApiController<T, String> {

    private final S service;

    public ClientRestController(S service) {
        super(service);
        this.service = service;
    }

    @Override
    public ApiResponseDto get(@RequestParam(value = "client_id") String id) throws Exception {
        return super.get(id);
    }

    @Override
    public ApiResponseDto delete(@RequestParam(value = "client_id") String id) throws Exception {
        return super.delete(id);
    }

    @PutMapping("secret")
    public ApiResponseDto updateSecret(@RequestParam(value = "client_id") String id) throws Exception {
        return new ApiResponseDto(true, this.service.updateSecret(id));
    }

    @PutMapping("grant_types")
    public ApiResponseDto updateGrantTypes(
            @RequestParam(value = "client_id") String id,
            @RequestParam(value = "grant_types") String[] grantTypes
    ) throws Exception {
        return new ApiResponseDto(true, this.service.updateGrantTypes(id, grantTypes));
    }

    @PutMapping("validity")
    public ApiResponseDto updateValidity(
            @RequestParam(value = "client_id") String id,
            @RequestParam(value = "access") Integer access,
            @RequestParam(value = "refresh", required = false) Integer refresh
    ) throws Exception {
        return new ApiResponseDto(true, this.service.updateValidity(id, access, refresh));
    }

}
