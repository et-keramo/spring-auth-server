package et.keramo.authsvr.service.user;

import et.keramo.authsvr.repository.user.LocalUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class LocalUserDto extends UserDto {

    private String roles;

    public LocalUserDto(LocalUser entity) {
        this.setUserId(entity.getUserId());
        this.roles = entity.getRoles();
    }

    public LocalUserDto(UserInfoDto userInfoDto) {
        this.setUserId(userInfoDto.getUserId());
    }

    public LocalUser toEntity() {
        return LocalUser.builder()
                .userId(this.getUserId())
                .password(this.getPassword())
                .roles(this.roles)
                .build();
    }

}
