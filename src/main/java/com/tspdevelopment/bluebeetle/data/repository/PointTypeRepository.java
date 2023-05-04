/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.bluebeetle.data.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import com.tspdevelopment.bluebeetle.data.model.PointType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author tobiesp
 */
public interface PointTypeRepository extends JpaRepository<PointType, UUID> {
    public List<PointType> findByPointCategoryAndGroup(PointCategory category, Group group);
    
    public List<PointType> findByPointCategory(PointCategory category);

    public List<PointType> findByGroup(Group group);
    
    public Page<PointType> findByPointCategoryAndGroup(PointCategory category, Group group, Pageable pageable);
    
    public Page<PointType> findByPointCategory(PointCategory category, Pageable pageable);

    public Page<PointType> findByGroup(Group group, Pageable pageable);

    
    
}
