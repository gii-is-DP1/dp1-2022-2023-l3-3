package sevenislands.admin;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


import sevenislands.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sevenislands.user.UserService;
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

    @Transactional
    public Page<User> findAllUser(Pageable pageable){
        return adminRepository2.findAll(pageable);
    }

}
