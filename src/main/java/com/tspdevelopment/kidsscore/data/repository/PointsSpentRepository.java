/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.kidsscore.data.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tspdevelopment.kidsscore.data.model.PointsSpent;
import com.tspdevelopment.kidsscore.data.model.Student;

/**
 *
 * @author tobiesp
 */
public interface PointsSpentRepository extends JpaRepository<PointsSpent, UUID> {
    public Optional<PointsSpent> findByStudent(Student student);
    public List<PointsSpent> findByEventDate(Date eventDate);
    @Query("SELECT u FROM PointsSpent u WHERE u.eventDate >= ?1 and u.eventDate <= ?2")
    public List<PointsSpent> searchEventDate(Date start, Date end);
    
}
