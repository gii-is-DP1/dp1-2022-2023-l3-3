package sevenislands.admin;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;


import sevenislands.user.User;

import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sevenislands.user.UserService;
import sevenislands.user.Enums.UserTypeEnum;

import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private AdminRepository adminRepository;
    private AdminRepository2 adminRepository2;
	private UserService userService;

    @Autowired
    public AdminService(AdminRepository adminRepository, UserService userService,AdminRepository2 adminRepository2) {
        this.adminRepository = adminRepository;
        this.userService = userService;
        this.adminRepository2 = adminRepository2;
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

    @Transactional
    public Page<User> findAllUser(Pageable pageable){
        return adminRepository2.findAll(pageable);
    }

}
