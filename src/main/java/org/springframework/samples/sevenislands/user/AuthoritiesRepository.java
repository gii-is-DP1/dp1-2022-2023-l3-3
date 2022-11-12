package org.springframework.samples.sevenislands.user;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;



public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{
	
    @Query("SELECT DISTINCT authorities.authority FROM Authorities authorities")
    public List<String> findAuthorities();

    @Modifying
    @Query(value = "DELETE FROM AUTHORITIES a WHERE a.user_id=id", nativeQuery = true)
    public void delete(@Param("id") Integer id);
}
