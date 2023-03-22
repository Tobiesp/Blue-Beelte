package com.tspdevelopment.KidsScore.provider.sqlprovider;

import com.tspdevelopment.KidsScore.helper.TestEntityGenerator;
import com.tspdevelopment.KidsScore.repositories.UserRepositoryTestEmbedded;
import com.tspdevelopment.kidsscore.KidsScoreApplication;
import com.tspdevelopment.kidsscore.data.model.User;
import com.tspdevelopment.kidsscore.data.repository.RoleRepository;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.UserProviderImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author tobiesp
 */
@SpringBootTest(classes = KidsScoreApplication.class)
@ActiveProfiles("test")
public class UserProviderImplIT {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserRepositoryTestEmbedded.class);
    
    public UserProviderImplIT() {
        TestEntityGenerator.getInstance().setRoleRepository(roleRepository);
        TestEntityGenerator.getInstance().setUserRepository(userRepository);
    }
    
    @BeforeAll
    public static void setUpClass() {
        
    }

    /**
     * Test of findAll method, of class UserProviderImpl.
     */
    @Test
    public void testFindAll() {
        System.out.println("findAll");
        UserProviderImpl instance = new UserProviderImpl(userRepository);
        List<User> expResult = userRepository.findAll();
        List<User> result = instance.findAll();
        assertEquals(expResult, result);
    }

    /**
     * Test of findById method, of class UserProviderImpl.
     */
    @Test
    public void testFindById() {
        System.out.println("findById");
        UUID id = userRepository.findAll().get(0).getId();
        UserProviderImpl instance = new UserProviderImpl(userRepository);
        Optional<User> expResult = userRepository.findById(id);
        Optional<User> result = instance.findById(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of update method, of class UserProviderImpl.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        User replaceItem = userRepository.findAll().get(0);
        replaceItem.setFullName("Thomas James");
        UUID id = userRepository.findAll().get(0).getId();
        UserProviderImpl instance = new UserProviderImpl(userRepository);
        User expResult = replaceItem;
        User result = instance.update(replaceItem, id);
        assertEquals(expResult, result);
    }

    /**
     * Test of delete method, of class UserProviderImpl.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        UUID id = userRepository.findAll().get(2).getId();
        UserProviderImpl instance = new UserProviderImpl(userRepository);
        instance.delete(id);
        Optional<User> result = instance.findById(id);
        assertEquals(false, result.isPresent());
    }

    /**
     * Test of search method, of class UserProviderImpl.
     */
    @Test
    public void testSearch() {
        System.out.println("search");
        User item = userRepository.findAll().get(0);
        UserProviderImpl instance = new UserProviderImpl(userRepository);
        List<User> expResult = new ArrayList<>();
        expResult.add(userRepository.findAll().get(0));
        List<User> result = instance.search(item);
        assertEquals(expResult, result);
    }

    /**
     * Test of create method, of class UserProviderImpl.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        User item = new User();
        item.setEmail("Test@email.com");
        item.setFullName("Test User");
        item.setPassword("Password_1test!");
        item.setUsername("testUser");
        UserProviderImpl instance = new UserProviderImpl(userRepository);
        User result = instance.create(item);
        User expResult = instance.findById(result.getId()).get();
        assertEquals(expResult, result);
    }

    /**
     * Test of updatePassowrd method, of class UserProviderImpl.
     */
    @Test
    public void testUpdatePassowrd() {
        System.out.println("updatePassowrd");
        UUID id = userRepository.findAll().get(0).getId();
        String password = "test#Password1$";
        UserProviderImpl instance = new UserProviderImpl(userRepository);
        String expResult = userRepository.findById(id).get().getPassword();
        String result = instance.updatePassowrd(id, password).getPassword();
        assertNotEquals(expResult, result);
    }

    /**
     * Test of updateJwtTokenId method, of class UserProviderImpl.
     */
    @Test
    public void testUpdateJwtTokenId() {
        System.out.println("updateJwtTokenId");
        UUID userId = userRepository.findAll().get(0).getId();
        UUID tokenId = UUID.randomUUID();
        UserProviderImpl instance = new UserProviderImpl(userRepository);
        instance.updateJwtTokenId(userId, tokenId);
        UUID result = instance.findById(userId).get().getTokenId();
        assertEquals(result, tokenId);
    }
    
}
