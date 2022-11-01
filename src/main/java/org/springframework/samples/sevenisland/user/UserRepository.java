package org.springframework.samples.sevenisland.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * Retrieve an 'User' from the data store by id.
     * 
     * @param id
     * @return an 'User' if found
     */
    @Query("SELECT user FROM User user WHERE user.id =: id")
    public User findById(@Param("id") int id);

    /**
     * Retrieve an 'User' from the data store by nickname.
     * 
     * @param id
     * @return an 'User' if found
     */
    @Query("SELECT user FROM User user WHERE user.nickname =: nickname")
    public User findByNickname(@Param("nickname") String nickname);

    /**
     * Delete an 'User' from the data store by id.
     * 
     * @param id
     */
    @Query("DELETE FROM User user WHERE user.id =: id")
    public void deleteById(@Param("id") int id);

    /**
     * Delete an 'User' from the data store by nickname.
     * 
     * @param nickname
     */
    @Query("DELETE FROM User user WHERE user.nickname =: nickname")
    public void delete(@Param("nickname") String nickname);
}
