package com.tspdevelopment.bluebeetle.provider.interfaces;

import java.util.List;

import com.tspdevelopment.bluebeetle.data.model.PointsSpent;
import com.tspdevelopment.bluebeetle.data.model.Student;
import java.time.LocalDate;

public interface PointsSpentProvider extends BaseProvider<PointsSpent>{
    public List<PointsSpent> findByStudent(Student student);
    
    public List<PointsSpent> findByEventDate(LocalDate eventDate);
    
    public List<PointsSpent> searchEventDate(LocalDate start, LocalDate end);
    
    public List<PointsSpent> searchStudentEventDate(Student student, LocalDate start, LocalDate end);
    
}
