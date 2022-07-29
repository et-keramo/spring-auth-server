package et.keramo.authsvr.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalUserRepository extends JpaRepository<LocalUser, String> {

}
