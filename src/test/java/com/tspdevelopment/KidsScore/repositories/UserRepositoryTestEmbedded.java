/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.KidsScore.repositories;

import com.tspdevelopment.kidsscore.data.model.User;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import static org.assertj.core.api.Java6Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author tobiesp
 */
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTestEmbedded {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser() {
        User user = new User();
        user.setFullName("Test User");
        user.setPassword("S3cret password");
        user.setEnabled(true);
        user.setUsername("test user");
        User savedUser = userRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("userId").isEqualTo(user);
    }
    
    @Test
    public void shouldUpdatePasswordUser() {
        User user = new User();
        user.setFullName("Test User");
        user.setPassword("S3cret password");
        user.setEnabled(true);
        user.setUsername("test user");
        User savedUser = userRepository.save(user);
        savedUser.setPassword("S3cr3t password");
        User updatedUser = userRepository.save(savedUser);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("userId").isEqualTo(updatedUser);
    }

}
