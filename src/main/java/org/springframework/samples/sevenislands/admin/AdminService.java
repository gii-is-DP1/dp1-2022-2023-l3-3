package org.springframework.samples.sevenislands.admin;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.sevenislands.user.AuthoritiesService;
import org.springframework.samples.sevenislands.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private AdminRepository adminRepository;
	private UserService userService;
	private AuthoritiesService authoritiesService;

    @Autowired
    public AdminService(AdminRepository adminRepository, UserService userService, AuthoritiesService authoritiesService) {
        this.adminRepository = adminRepository;
        this.userService = userService;
        this.authoritiesService = authoritiesService;
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
		authoritiesService.saveAuthorities(admin.getId(), "admin");
	}

    @Transactional
    public void save(Admin admin) {
        adminRepository.save(admin);
    }
}
