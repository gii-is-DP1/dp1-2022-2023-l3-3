package org.springframework.samples.sevenislands.admin;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Integer> {

    /*@Query("SELECT admin FROM User user WHERE user.id=:id")
    public Admin findById(@Param("id") int id);*/
}
