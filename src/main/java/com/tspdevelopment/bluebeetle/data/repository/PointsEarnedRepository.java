package com.tspdevelopment.bluebeetle.data.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tspdevelopment.bluebeetle.data.model.PointsEarned;
import com.tspdevelopment.bluebeetle.data.model.Student;
import java.time.LocalDate;

/**
 *
 * @author tobiesp
 */
public interface PointsEarnedRepository extends JpaRepository<PointsEarned, UUID> {
    public List<PointsEarned> findByStudent(Student student);
    public List<PointsEarned> findByEventDate(LocalDate eventDate);
    @Query("SELECT u FROM PointsEarned u WHERE u.eventDate >= ?1 and u.eventDate <= ?2")
    public List<PointsEarned> searchEventDate(LocalDate start, LocalDate end);
    @Query("SELECT u FROM PointsEarned u WHERE u.student = ?1 and u.eventDate >= ?2 and u.eventDate <= ?3")
    public List<PointsEarned> searchStudentEventDate(Student student, LocalDate start, LocalDate end);
    @Query("SELECT u.eventDate FROM PointsEarned u ORDER BY u.eventDate DESC")
    public List<LocalDate> getLastEventDate();
    @Query("SELECT sum(u.total) FROM PointsEarned u WHERE u.student = ?1")
    public Long getPointSum(Student student);
    
}
