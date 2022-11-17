package sevenislands.user;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT user FROM User user")
    public List<User> findAll();

    @Query("SELECT user FROM User user WHERE user.id=:id")
    public Optional<User> findById(@Param("id") int id) throws DataAccessException;

    @Query("SELECT user FROM User user WHERE user.nickname=:nickname")
    public Optional<User> findByNickname(@Param("nickname") String nickname) throws DataAccessException;

    @Query("SELECT user FROM User user WHERE user.email=:email")
    public Optional<User> findByEmail(@Param("email") String email) throws DataAccessException;

    @Query("DELETE FROM User user WHERE user.id=:id")
    public void deleteById(@Param("id") int id);

    @Query("DELETE FROM User user WHERE user.nickname=:nickname")
    public void deleteByNickname(@Param("nickname") String nickname);

    @Query("SELECT count(user) = 1 FROM User user WHERE user.nickname=?1")
    public Boolean checkUserNickname(String nickname);

    @Query("SELECT count(user) = 1 FROM User user WHERE user.email=?1")
    public Boolean checkUserEmail(String email);

    @Query("SELECT DISTINCT user.userType FROM User user")
    public List<String> findAuthorities();
}
