package sevenislands.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AdminRepositoryTest {
    

    @Autowired
    AdminRepository adminRepository;

    @Test
    public void TestDataAllFindAllSuccess(){
        List<Admin> admin= new ArrayList<>();
        adminRepository.findAll().iterator().forEachRemaining(admin::add);

        assertNotNull(admin);
        assertFalse(admin.isEmpty());
        assertEquals(6, admin.size());
    }
}
