package sevenislands.punctuation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sevenislands.game.Game;

@Repository
public interface PunctuationRepository extends CrudRepository<Punctuation, Integer>{

    @Query("SELECT u, SUM(p.punctuation) FROM Punctuation p INNER JOIN p.user u GROUP BY u ORDER BY SUM(p.punctuation) DESC")
    List<Object []> findPunctuations();

    @Query("SELECT u, p.punctuation FROM Punctuation p INNER JOIN p.user u INNER JOIN p.game g WHERE g.id=?1 ORDER BY p.punctuation DESC")
    List<Object []> findPunctuationByGame(Integer game);

    @Query("SELECT COUNT(u)=1 FROM Punctuation p INNER JOIN p.user u INNER JOIN p.game g WHERE g.id=?1 AND u.nickname=?2")
    Boolean findPunctuationByGame(Integer game, String nickname);
    
    @Query("SELECT SUM(p.punctuation) FROM Punctuation p INNER JOIN p.user u WHERE u.nickname=?1")
    Long findPunctuationByNickname(String nickname);
}
