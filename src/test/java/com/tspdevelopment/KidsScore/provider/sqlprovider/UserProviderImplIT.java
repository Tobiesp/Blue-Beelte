/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.tspdevelopment.KidsScore.provider.sqlprovider;

import com.tspdevelopment.KidsScore.helper.TestEntityGenerator;
import com.tspdevelopment.KidsScore.repositories.UserRepositoryTestEmbedded;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author tobiesp
 */
@DataJpaTest
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
        UUID id = userRepository.findAll().get(3).getId();
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
        User item = null;
        UserProviderImpl instance = new UserProviderImpl(userRepository);
        User expResult = null;
        User result = instance.create(item);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updatePassowrd method, of class UserProviderImpl.
     */
    @Test
    public void testUpdatePassowrd() {
        System.out.println("updatePassowrd");
        UUID id = userRepository.findAll().get(0).getId();
        String password = "";
        UserProviderImpl instance = new UserProviderImpl(userRepository);
        User expResult = null;
        User result = instance.updatePassowrd(id, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateJwtTokenId method, of class UserProviderImpl.
     */
    @Test
    public void testUpdateJwtTokenId() {
        System.out.println("updateJwtTokenId");
        UUID userId = userRepository.findAll().get(0).getId();
        UUID tokenId = null;
        UserProviderImpl instance = new UserProviderImpl(userRepository);
        instance.updateJwtTokenId(userId, tokenId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
