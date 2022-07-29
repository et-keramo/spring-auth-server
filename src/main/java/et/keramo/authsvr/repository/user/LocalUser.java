package et.keramo.authsvr.repository.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Entity(name = "oauth_user")
public class LocalUser {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "roles")
    private String roles;

    @CreationTimestamp
    @Column(name = "reg_dt", nullable = false, updatable = false)
    private Timestamp regDt;

    @UpdateTimestamp
    @Column(name = "mod_dt")
    private Timestamp modDt;

    @Builder
    public LocalUser(
            String userId,
            String password,
            String roles
    ) {
        this.userId = userId;
        this.password = password;
        this.roles = roles;
    }

}
