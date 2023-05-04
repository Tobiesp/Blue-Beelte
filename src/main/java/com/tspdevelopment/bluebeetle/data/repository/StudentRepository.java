/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.bluebeetle.data.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author tobiesp
 */
public interface StudentRepository extends JpaRepository<Student, UUID> {
    public Optional<Student> findByName(String name);
    
    public List<Student> findByNameLike(String name);
    
    public List<Student> findByGroup(Group group);
    
    public List<Student> findByGrade(int grade);
    
    public List<Student> findByGraduated(Date graduated);
    
    @Query("SELECT u FROM Student u WHERE u.graduated >= ?1 and u.graduated <= ?2")
    public List<Student> searchGraduated(Date start, Date end);
    
    public Page<Student> findByNameLike(String name, Pageable pageable);
    
    public Page<Student> findByGraduated(Date graduated, Pageable pageable);
    
    @Query("SELECT u FROM Student u WHERE u.graduated >= ?1 and u.graduated <= ?2")
    public Page<Student> searchGraduated(Date start, Date end, Pageable pageable);
    
    public Page<Student> findByGroup(Group group, Pageable pageable);
    
    public Page<Student> findByGrade(int grade, Pageable pageable);
    
}
