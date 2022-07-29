package et.keramo.authsvr.api.rest;

import et.keramo.authsvr.service.ApiService;
import et.keramo.common.api.ApiResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * API 공통
 *
 * @param <T> DTO 타입
 * @param <ID> ID 자료형 타입
 */
@RequiredArgsConstructor
public abstract class ApiController<T, ID> {

    private final ApiService<T, ID> service;

    @GetMapping("list")
    public ApiResponseDto list() throws Exception {
        return new ApiResponseDto(true, this.service.list(), "목록 조회 성공");
    }

    @GetMapping
    public ApiResponseDto get(@RequestParam(value = "id") ID id) throws Exception {
        return new ApiResponseDto(true, this.service.get(id), "조회 성공");
    }

    @PostMapping
    public ApiResponseDto add(@RequestBody T object) throws Exception {
        return new ApiResponseDto(true, service.add(object), "등록 성공");
    }

    @PutMapping
    public ApiResponseDto update(@RequestBody T object) throws Exception {
        return new ApiResponseDto(true, this.service.update(object), "수정 성공");
    }

    @DeleteMapping
    public ApiResponseDto delete(@RequestParam(value = "id") ID id) throws Exception {
        this.service.delete(id);
        return new ApiResponseDto(true, "삭제 성공");
    }

}
