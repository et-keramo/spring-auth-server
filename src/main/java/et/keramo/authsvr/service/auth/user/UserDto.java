package et.keramo.authsvr.service.auth.user;

import et.keramo.authsvr.repository.rdb.auth.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDto {

    private String username;
    private String password;
    private String roles;

    public UserDto(User entity) {
        this.username = entity.getUsername();
        this.roles = entity.getRoles();
    }

    public User toEntity() {
        return User.builder()
                .username(this.username)
                .password(this.password)
                .roles(this.roles)
                .build();
    }

}
