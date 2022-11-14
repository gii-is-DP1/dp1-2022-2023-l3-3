package sevenislands.admin;


import org.springframework.data.jpa.repository.JpaRepository;

import sevenislands.user.User;

public interface AdminRepository2 extends JpaRepository<User,Long>{

}
