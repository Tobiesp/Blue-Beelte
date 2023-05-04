package com.tspdevelopment.bluebeetle.provider.interfaces;

import com.tspdevelopment.bluebeetle.data.model.Group;
import java.util.Date;
import java.util.List;

import com.tspdevelopment.bluebeetle.data.model.Student;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentProvider extends BaseProvider<Student> {
    public Optional<Student> findByName(String name);
    
    public List<Student> findByNameLike(String name);
    
    public List<Student> findByGroup(Group group);
    
    public List<Student> findByGrade(int grade);

    public List<Student> findByGraduated(Date graduated);

    public List<Student> searchGraduated(Date start, Date end);
    
    public Page<Student> findByNameLike(String name, Pageable pageable);

    public Page<Student> findByGraduated(Date graduated, Pageable pageable);

    public Page<Student> searchGraduated(Date start, Date end, Pageable pageable);
    
    public Page<Student> findByGroup(Group group, Pageable pageable);
    
    public Page<Student> findByGrade(int grade, Pageable pageable);
    
}
