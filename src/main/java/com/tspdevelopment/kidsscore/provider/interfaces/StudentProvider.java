package com.tspdevelopment.kidsscore.provider.interfaces;

import com.tspdevelopment.kidsscore.data.model.Group;
import java.util.Date;
import java.util.List;

import com.tspdevelopment.kidsscore.data.model.Student;
import java.util.Optional;

public interface StudentProvider extends BaseProvider<Student> {
    public Optional<Student> findByName(String name);
    
    public List<Student> findByNameLike(String name);
    
    public Optional<List<Student>> findByGroup(Group group);
    
    public Optional<List<Student>> findByGrade(int grade);

    public List<Student> findByGraduated(Date graduated);

    public List<Student> searchGraduated(Date start, Date end);
    
}
