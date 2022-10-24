package org.springframework.samples.admin;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends Repository<Admin, Integer>{
    
    void save(Admin admin) throws DataAccessException;

    @Query("SELECT DISTINCT admin FROM Admin admin WHERE admin.name LIKE :name%")
	public Collection<Admin> findByName(@Param("name") String name);

    @Query("SELECT admin FROM Admin player WHERE admin.id = :id")
	public Admin findById(@Param("id") Integer id);

    @Query("SELECT DISTINCT admin FROM Admin admin WHERE admin.email = :email")
	public Collection<Admin> findEmail(@Param("email") String email);
}
