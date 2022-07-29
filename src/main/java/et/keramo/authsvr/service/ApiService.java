package et.keramo.authsvr.service;

import java.util.List;

/**
 * API 서비스 공통
 *
 * @param <T> DTO 타입
 * @param <ID> ID 자료형 타입
 */
public interface ApiService<T, ID> {

    List<T> list() throws Exception;
    T get(ID id) throws Exception;
    T add(T object) throws Exception;
    T update(T object) throws Exception;
    void delete(ID id) throws Exception;

}
