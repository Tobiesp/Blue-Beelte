/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.bluebeetle.data.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.*;

import com.tspdevelopment.bluebeetle.data.model.User;

/**
 *
 * @author tobiesp
 */

public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findByUsername(String username);
}
