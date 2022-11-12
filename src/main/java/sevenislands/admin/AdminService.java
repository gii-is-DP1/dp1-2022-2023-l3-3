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

    @Transactional(readOnly = true)
    public Optional<Admin> findAdmin(Integer id) {
        return adminRepository.findById(id);
    }

    @Transactional
	public void saveNewAdmin(Admin admin) throws DataAccessException {
        admin.setEnabled(true);
        admin.setAvatar("adminAvatar.png");
		admin.setCreationDate(new Date(System.currentTimeMillis()));
		adminRepository.save(admin);
		userService.save(admin);
	}

    @Transactional
    public void save(Admin admin) {
        adminRepository.save(admin);
    }

    @Transactional
    public void update(Admin admin) {
        adminRepository.updateAdmin(admin, admin.getId());
    }

    @Transactional
	public void remove(Admin admin) {
		adminRepository.delete(admin);
	}

}
