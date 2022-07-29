package et.keramo.authsvr.service.client;

import et.keramo.authsvr.service.ApiService;

/**
 * 클라이언트 서비스 공통
 *
 * @param <T> 클라이언트 DTO 타입
 */
public interface ClientService<T extends ClientDto> extends ApiService<T, String> {

    T updateSecret(String clientId) throws Exception;
    T updateGrantTypes(String clientId, String[] grantTypes) throws Exception;
    T updateValidity(String clientId, Integer access, Integer refresh) throws Exception;

}
