package sevenislands.admin;

import sevenislands.user.User;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Integer> {

	/**
	 * Actualiza los datos del administrador.
	 * @param admin
	 * @param admin_id
	 */
    @Modifying
	@Query("UPDATE User user SET user=?1 WHERE user.id=?2")
	public void updateAdmin(Admin admin, Integer admin_id);
}
