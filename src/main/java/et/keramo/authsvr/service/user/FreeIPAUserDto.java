package et.keramo.authsvr.service.user;

import com.fasterxml.jackson.databind.JsonNode;
import et.keramo.authsvr.util.JacksonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static et.keramo.authsvr.constants.keycloak.FreeIPAConst.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class FreeIPAUserDto extends UserDto {

    private String roles;
    private String givenname;
    private String sn;
    private String cn;
    private String mail;

    public FreeIPAUserDto(JsonNode jsonNode) {
        this.setUserId(JacksonUtil.findFirstText(jsonNode, UID_KEY));
        this.givenname = JacksonUtil.findFirstText(jsonNode, GIVENNAME_KEY);
        this.sn = JacksonUtil.findFirstText(jsonNode, SN_KEY);
        this.cn = JacksonUtil.findFirstText(jsonNode, CN_KEY);
        this.mail = JacksonUtil.findFirstText(jsonNode, MAIL_KEY);
    }

    public FreeIPAUserDto(UserInfoDto userInfoDto) {
        this.setUserId(userInfoDto.getUserId());
        this.givenname = userInfoDto.getName();
        this.sn = userInfoDto.getName();
        this.cn = userInfoDto.getName();
    }

}
