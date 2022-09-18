/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.kidsscore.data.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.Student;

/**
 *
 * @author tobiesp
 */
public interface StudentRepository extends JpaRepository<Student, UUID> {
    public Optional<Student> findByName(String name);
    public Optional<List<Student>> findByGroup(Group group);
    public Optional<List<Student>> findByGrade(int grade);
    
}
