package com.tspdevelopment.kidsscore.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.kidsscore.data.model.Role;

/**
 *
 * @author tobiesp
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {
    
}
