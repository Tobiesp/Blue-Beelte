package com.tspdevelopment.bluebeetle.repositories;

import com.tspdevelopment.bluebeetle.helper.TestEntityGenerator;
import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.data.repository.RoleRepository;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import static org.assertj.core.api.Java6Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class UserRepositoryTestEmbedded {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserRepositoryTestEmbedded.class);
    
    @BeforeEach
    public void setUp() {
        TestEntityGenerator.getInstance().setRoleRepository(roleRepository);
        TestEntityGenerator.getInstance().setUserRepository(userRepository);
        
    }

    @Test
    public void shouldSaveUser() {
        User user = TestEntityGenerator.getInstance().generateUser();
        log.info(user.toString());
        User savedUser = userRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("id").isEqualTo(user);
    }
    
    @Test
    public void shouldUpdatePasswordUser() {
        User user = TestEntityGenerator.getInstance().generateUser();
        user.setPassword("S3cr3t password");
        User updatedUser = userRepository.save(user);
        assertThat(user).usingRecursiveComparison().ignoringFields("id").isEqualTo(updatedUser);
    }

}
