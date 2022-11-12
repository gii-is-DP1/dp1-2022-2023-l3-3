package org.springframework.samples.sevenislands.user;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.sevenislands.lobby.Lobby;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT user FROM User user WHERE user.id=:id")
    public Optional<User> findById(@Param("id") int id) throws DataAccessException;

    @Query("SELECT user FROM User user WHERE user.nickname=:nickname")
    public Optional<User> findByNickname(@Param("nickname") String nickname) throws DataAccessException;

    @Query("DELETE FROM User user WHERE user.id=:id")
    public void deleteById(@Param("id") int id);

    @Query("DELETE FROM User user WHERE user.nickname=:nickname")
    public void delete(@Param("nickname") String nickname);

    @Query("SELECT count(user) = 1 FROM User user WHERE user.nickname=?1")
    public boolean checkUser(String nickname);

    @Query("SELECT count(user.lobby) = 1 FROM User user WHERE user.nickname=?1")
    public boolean checkUserLobby(String nickname);

    @Modifying
    @Query("UPDATE User user SET user=?1 WHERE user.id=?2")
    public void updateUser(User user, Integer user_id);
}
