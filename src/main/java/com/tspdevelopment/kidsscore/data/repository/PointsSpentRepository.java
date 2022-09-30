package com.tspdevelopment.kidsscore.data.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tspdevelopment.kidsscore.data.model.PointsSpent;
import com.tspdevelopment.kidsscore.data.model.Student;
import java.time.LocalDate;

/**
 *
 * @author tobiesp
 */
public interface PointsSpentRepository extends JpaRepository<PointsSpent, UUID> {
    public List<PointsSpent> findByStudent(Student student);
    public List<PointsSpent> findByEventDate(LocalDate eventDate);
    @Query("SELECT u FROM PointsSpent u WHERE u.eventDate >= ?1 and u.eventDate <= ?2")
    public List<PointsSpent> searchEventDate(LocalDate start, LocalDate end);
    @Query("SELECT u FROM PointsSpent u WHERE u.student= ?1 and u.eventDate >= ?2 and u.eventDate <= ?3")
    public List<PointsSpent> searchStudentEventDate(Student student, LocalDate start, LocalDate end);
    
}
