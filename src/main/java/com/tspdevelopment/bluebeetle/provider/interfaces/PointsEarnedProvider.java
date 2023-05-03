package com.tspdevelopment.bluebeetle.provider.interfaces;

import java.util.List;

import com.tspdevelopment.bluebeetle.data.model.PointsEarned;
import com.tspdevelopment.bluebeetle.data.model.Student;
import java.time.LocalDate;

public interface PointsEarnedProvider extends BaseProvider<PointsEarned>{
    
    public List<PointsEarned> findByStudent(Student student);
    
    public List<PointsEarned> findByEventDate(LocalDate eventDate);
    
    public List<PointsEarned> searchEventDate(LocalDate start, LocalDate end);
    
    public List<PointsEarned> searchStudentEventDate(Student student, LocalDate start, LocalDate end);
    
    public List<PointsEarned> findByStudentAndEventDate(Student student, LocalDate eventDate);
    
    public LocalDate getLastEventDate();
    
}
