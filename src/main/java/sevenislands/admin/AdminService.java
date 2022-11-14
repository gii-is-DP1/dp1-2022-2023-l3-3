package sevenislands.admin;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import sevenislands.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private AdminRepository adminRepository;
	private UserService userService;

    @Autowired
    public AdminService(AdminRepository adminRepository, UserService userService) {
        this.adminRepository = adminRepository;
        this.userService = userService;
    }

    /**
     * Encuentra un admin dado su id.
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Admin> findAdmin(Integer id) {
        return adminRepository.findById(id);
    }

    /**
     * Guarda un usuario nuevo.
     * <p> Esta función es necesaria ya que le establece el avatar por defecto y su fecha de cración.
     * @param admin
     * @throws DataAccessException
     */
    @Transactional
	public void saveNewAdmin(Admin admin) throws DataAccessException {
        admin.setEnabled(true);
        admin.setAvatar("adminAvatar.png");
		admin.setCreationDate(new Date(System.currentTimeMillis()));
		adminRepository.save(admin);
		userService.save(admin);
	}

    /**
     * Guarda o actualiza un administrador.
     * @param admin
     */
    @Transactional
    public void save(Admin admin) {
        adminRepository.save(admin);
    }

    /**
     * Actualiza el administrador dado.
     * @param admin
     */
    @Transactional
    public void update(Admin admin) {
        adminRepository.updateAdmin(admin, admin.getId());
    }

    /**
     * Elimina un administrador de la base de datos.
     * @param admin
     */
    @Transactional
	public void remove(Admin admin) {
		adminRepository.delete(admin);
	}

}
