/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.kidsscore.data.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.kidsscore.data.model.RunningTotals;
import com.tspdevelopment.kidsscore.data.model.Student;

/**
 *
 * @author tobiesp
 */
public interface RunningTotalsRepository extends JpaRepository<RunningTotals, UUID> {
    public Optional<RunningTotals> findByStudent(Student student);
    
}
