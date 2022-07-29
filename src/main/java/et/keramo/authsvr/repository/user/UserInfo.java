package et.keramo.authsvr.repository.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Entity(name = "oauth_user_info")
public class UserInfo {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_nm")
    private String name;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "user_info")
    private String userInfo;

    @CreationTimestamp
    @Column(name = "req_dt", nullable = false, updatable = false)
    private Timestamp reqDt;

    @Column(name = "confirm_dt")
    private Timestamp confirmDt;

    @Builder
    public UserInfo(
            String userId,
            String name,
            String clientId,
            String groupId,
            String userInfo,
            Timestamp confirmDt
    ) {
        this.userId = userId;
        this.name = name;
        this.clientId = clientId;
        this.groupId = groupId;
        this.userInfo = userInfo;
        this.confirmDt = confirmDt;
    }

}
