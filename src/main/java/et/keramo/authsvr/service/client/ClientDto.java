package et.keramo.authsvr.service.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientDto {

    public static final Integer DEFAULT_ACCESS_EXPIRED = 3600;
    public static final Integer DEFAULT_REFRESH_EXPIRED = 10800;

    @NotBlank(message = "클라이언트 ID는 필수값입니다.")
    private String clientId;

    private String secret;

    @NotEmpty(message = "인증 유형은 최소 1가지 이상 지정해야합니다.")
    private String[] grantTypes;

    @Min(value = 300, message = "액세스 토큰 유효시간은 최소 5분(360초) 입니다.")
    private Integer accessTokenValidity;

    @Min(value = 1800, message = "리프레시 토큰 유효시간은 최소 30분(1800초) 입니다.")
    private Integer refreshTokenValidity;

}
