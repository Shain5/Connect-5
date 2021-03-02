package Player;
import Player.Player;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
@EnableScan
public interface playerRepository extends CrudRepository<Player, String> {
    List<Player> findByName(String name);
    Optional<Player> findById(String id);

    Player findFirstByName(String name);
}
