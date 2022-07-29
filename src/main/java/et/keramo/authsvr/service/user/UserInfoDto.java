package et.keramo.authsvr.service.user;

import et.keramo.authsvr.repository.user.UserInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class UserInfoDto extends UserDto {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private String userInfo;
    private String reqDt;
    private String confirmDt;
    private boolean active;

    public UserInfoDto(UserInfo entity) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        this.setUserId(entity.getUserId());
        this.setName(entity.getName());
        this.setClientId(entity.getClientId());
        this.setGroupId(entity.getGroupId());
        this.userInfo = entity.getUserInfo();
        this.reqDt = entity.getReqDt() == null ? null : sdf.format(entity.getReqDt());
        this.confirmDt = entity.getConfirmDt() == null ? null : sdf.format(entity.getConfirmDt());
        this.active = entity.getConfirmDt() != null;
    }

    public UserInfo toEntity() {
        return UserInfo.builder()
                .userId(this.getUserId())
                .name(this.getName())
                .clientId(this.getClientId())
                .groupId(this.getGroupId())
                .userInfo(this.userInfo)
                .confirmDt(this.confirmDt == null ? null : Timestamp.valueOf(this.confirmDt))
                .build();
    }

}
