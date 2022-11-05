/* 
package org.springframework.samples.petclinic.player;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends CrudRepository<Player, String> {

   
    @Query("SELECT user FROM User user WHERE user.id =: id")
    public Player findById(@Param("id") int id);

    
    @Query("SELECT user FROM User user WHERE user.nickname =: nickname")
    public Player findByNickname(@Param("nickname") String nickname);

    @Query("DELETE FROM User user WHERE user.id =: id")
    public void deleteById(@Param("id") int id);

   
    @Query("DELETE FROM User user WHERE user.nickname =: nickname")
    public void delete(@Param("nickname") String nickname);
}
*/