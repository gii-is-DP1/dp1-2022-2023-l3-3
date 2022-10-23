package org.springframework.samples.sevenislands.user;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

public class AdminService {

    private AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Admin> findAdmin(Integer id) {
        // retrieving admin by id
        return adminRepository.findById(id);
    }
}
