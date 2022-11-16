package sevenislands.admin;

import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;



@DataJpaTest
public class AdminServiceTest {


   
       

       
    @Autowired
    AdminRepository adminRepository;


    @Test
    public void TestSave(){
        Admin admin=new Admin();
        Date fecha=new Date();
        admin.setNickname("admin2");
        admin.setPassword("12345789");
        admin.setEnabled(true);
        admin.setFirstName("Admin FN");
        admin.setLastName("Admin LN");
        admin.setEmail("admin2@sevenislands.com");
        admin.setCreationDate(fecha);
        admin.setUserType("admin");
        admin.setBirthDate(fecha);
        admin.setAvatar("adminAvatar.png");

        AdminService adminService=new AdminService(adminRepository, null, null);
        try{
            adminService.save(admin);
        }catch(Exception e){
            fail("This expeception should not be thrown!");
        }
    }


    @Test
    public void TestUpdate(){
        Admin admin=adminRepository.findById(1).get();
        
        AdminService adminService=new AdminService(adminRepository, null, null);
        
        admin.setNickname("antonio");
        admin.setEmail("antonio@gmail.com");
        try{
            adminService.update(admin);
        }catch(Exception e){
            fail("This expeception should not be thrown!");
        }
    }

    @Test
    public void TestDelete(){
        Admin admin=adminRepository.findById(1).get();
        
        AdminService adminService=new AdminService(adminRepository, null, null);
       
        try{
            adminService.remove(admin);
        }catch(Exception e){
            fail("This expeception should not be thrown!");
        }
    }
}
