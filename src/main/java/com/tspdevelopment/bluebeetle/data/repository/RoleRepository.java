package com.tspdevelopment.bluebeetle.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.bluebeetle.data.model.Role;
import java.util.Optional;

/**
 *
 * @author tobiesp
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {
    public Optional<Role> findByAuthority(String authority);
}
