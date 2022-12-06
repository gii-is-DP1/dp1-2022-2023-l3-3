package sevenislands.treasure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreasureRepository extends CrudRepository<Treasure, Integer> {

    public List<Treasure> findAll();

    @Query("SELECT treasure FROM Treasure treasure WHERE treasure.name=?1")
    public Optional<Treasure> findByName(String name);

}
