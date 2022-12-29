package sevenislands.punctuation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PunctuationRepository extends CrudRepository<Punctuation, Integer>{

    @Query("SELECT u, SUM(p.punctuation) FROM Punctuation p INNER JOIN p.user u GROUP BY u ORDER BY SUM(p.punctuation) DESC")
    List<Object []> findPunctuations();
    
}
