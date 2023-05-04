package com.tspdevelopment.bluebeetle.provider.interfaces;

import java.util.List;

import com.tspdevelopment.bluebeetle.data.model.PointsEarned;
import com.tspdevelopment.bluebeetle.data.model.Student;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointsEarnedProvider extends BaseProvider<PointsEarned>{
    
    public List<PointsEarned> findByStudent(Student student);
    
    public List<PointsEarned> findByEventDate(LocalDate eventDate);
    
    public List<PointsEarned> searchEventDate(LocalDate start, LocalDate end);
    
    public List<PointsEarned> searchStudentEventDate(Student student, LocalDate start, LocalDate end);
    
    public List<PointsEarned> findByStudentAndEventDate(Student student, LocalDate eventDate);
    
    public Page<PointsEarned> findByStudent(Student student, Pageable pageable);
    
    public Page<PointsEarned> findByEventDate(LocalDate eventDate, Pageable pageable);
    
    public Page<PointsEarned> searchEventDate(LocalDate start, LocalDate end, Pageable pageable);
    
    public Page<PointsEarned> searchStudentEventDate(Student student, LocalDate start, LocalDate end, Pageable pageable);
    
    public Page<PointsEarned> findByStudentAndEventDate(Student student, LocalDate eventDate, Pageable pageable);
    
    public LocalDate getLastEventDate();
    
}
