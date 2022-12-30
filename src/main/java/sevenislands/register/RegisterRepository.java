package sevenislands.register;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface RegisterRepository extends CrudRepository<Register, Integer>{
    
    @Query("SELECT a, r.acquisitionDate FROM Register r INNER JOIN r.achievement a INNER JOIN r.user u WHERE u.nickname = ?1")
    List<Object []> findArchievementByNickname(String nickname);

    @Query("SELECT a, COUNT(r) FROM Register r RIGHT JOIN r.achievement a GROUP BY a")
    List<Object []> countAchievements();
}
