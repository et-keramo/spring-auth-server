package et.keramo.authsvr.repository.rdb.auth.user;

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
public class User {

    @Id
    @Column(name = "user_id")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "roles")
    private String roles;

    @CreationTimestamp
    @Column(name = "reg_dt", nullable = false, updatable = false)
    protected Timestamp regDt;

    @UpdateTimestamp
    @Column(name = "mod_dt")
    protected Timestamp modDt;

    @Builder
    public User(
            String username,
            String password,
            String roles
    ) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

}
