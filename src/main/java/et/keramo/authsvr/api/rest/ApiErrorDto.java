package et.keramo.authsvr.api.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ApiErrorDto {

    private Integer status;
    private Object error;

    public ApiErrorDto(HttpStatus httpStatus, Object error) {
        this.status = httpStatus.value();
        this.error = error;
    }

}
