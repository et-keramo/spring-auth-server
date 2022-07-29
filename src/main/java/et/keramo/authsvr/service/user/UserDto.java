package et.keramo.authsvr.service.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {

    @NotBlank(message = "사용자 ID는 필수값입니다.")
    private String userId;

    private String password;

    @NotBlank(message = "사용자 명은 필수값입니다.")
    private String name;

    @NotBlank(message = "클라이언트 ID는 필수값입니다.")
    private String clientId;

    @NotBlank(message = "그룹 ID는 필수값입니다.")
    private String groupId;

}
