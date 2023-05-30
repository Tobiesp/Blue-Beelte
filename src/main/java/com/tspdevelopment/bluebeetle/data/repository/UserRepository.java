package com.tspdevelopment.bluebeetle.data.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.bluebeetle.data.model.User;

/**
 *
 * @author tobiesp
 */

public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findByUsername(String username);
    
    public Optional<User> findByEmail(String email);
}
