/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.kidsscore.data.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.PointCategory;
import com.tspdevelopment.kidsscore.data.model.PointTable;

/**
 *
 * @author tobiesp
 */
public interface PointTableRepository extends JpaRepository<PointTable, UUID> {
    public List<PointTable> findByPointCategory(PointCategory category);

    public List<PointTable> findByGroup(Group group);

    
    
}
