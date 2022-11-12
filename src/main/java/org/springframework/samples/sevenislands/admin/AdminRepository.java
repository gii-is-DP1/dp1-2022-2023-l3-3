package org.springframework.samples.sevenislands.admin;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Integer> {

    @Modifying
	@Query("UPDATE User user SET user=?1 WHERE user.id=?2")
	public void updateAdmin(Admin admin, Integer admin_id);
}
