/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.KidsScore.data.repository;

import com.tspdevelopment.KidsScore.data.model.PointsSpent;
import com.tspdevelopment.KidsScore.data.model.Student;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author tobiesp
 */
public interface PointsSpentRepository extends JpaRepository<PointsSpent, UUID> {
    public Optional<PointsSpent> findByName(Student student);
    
}
