package com.tspdevelopment.KidsScore.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.KidsScore.data.model.Role;

/**
 *
 * @author tobiesp
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {
    
}
