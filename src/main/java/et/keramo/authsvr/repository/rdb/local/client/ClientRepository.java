package et.keramo.authsvr.repository.rdb.local.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("clientRepository")
public interface ClientRepository extends JpaRepository<Client, String> {

}
