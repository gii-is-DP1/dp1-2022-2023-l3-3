package sevenislands.gameDetails;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameDetailsRepository extends CrudRepository<GameDetails, Integer>{

    @Query("SELECT u, SUM(gd.punctuation) FROM GameDetails gd INNER JOIN gd.user u GROUP BY u ORDER BY SUM(gd.punctuation) DESC")
    List<Object []> findPunctuations();

    @Query("SELECT u, gd.punctuation FROM GameDetails gd INNER JOIN gd.user u INNER JOIN gd.game g WHERE g.id=?1 ORDER BY gd.punctuation DESC")
    List<Object []> findPunctuationByGame(Integer game);

    @Query("SELECT COUNT(u)=1 FROM GameDetails gd INNER JOIN gd.user u INNER JOIN gd.game g WHERE g.id=?1 AND u.nickname=?2")
    Boolean checkPunctuationByGameAndUser(Integer game, String nickname);

    @Query("SELECT COUNT(gd)>0 FROM GameDetails gd INNER JOIN gd.game g WHERE g.id=?1")
    Boolean checkPunctuationByGame(Integer game);
    
    @Query("SELECT SUM(gd.punctuation) FROM GameDetails gd INNER JOIN gd.user u WHERE u.nickname=?1")
    Long findPunctuationByNickname(String nickname);

    @Query("SELECT COUNT(gd) FROM GameDetails gd INNER JOIN gd.winner w WHERE w.nickname=?1")
    Long findVictoriesByNickname(String nickname);

    @Query("SELECT COUNT(gd) FROM GameDetails gd INNER JOIN gd.winner w WHERE w.nickname=?1 AND gd.tieBreak=TRUE")
    Long findTieBreaksByNickname(String nickname);

    @Query("SELECT COUNT(gd) FROM GameDetails gd INNER JOIN gd.user u WHERE u.nickname=?1")
    Long findAllByNickname(String nickname);
}
