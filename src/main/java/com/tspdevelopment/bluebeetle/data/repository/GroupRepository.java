/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.bluebeetle.data.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.bluebeetle.data.model.Group;

/**
 *
 * @author tobiesp
 */
public interface GroupRepository extends JpaRepository<Group, UUID> {
    public Optional<Group> findByName(String name);

    public List<Group> findByNameLike(String name);
    
}
