package org.springframework.samples.sevenisland.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends CrudRepository<Admin, Integer> {

    @Query("SELECT admin FROM User user WHERE user.id =: id")
    public Admin findById(@Param("id") int id);
}
