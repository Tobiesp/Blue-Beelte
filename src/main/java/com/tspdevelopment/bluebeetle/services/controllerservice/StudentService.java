/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.StudentProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.StudentProviderImpl;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author tobiesp
 */
public class StudentService extends BaseService<Student, StudentProvider> {

    public StudentService(StudentRepository repository) {
        this.provider = new StudentProviderImpl(repository);
    }

    public List<Student> findByGraduated(Date graduated){
        return this.provider.findByGraduated(graduated);
    }

    public List<Student> searchGraduated(Date start, Date end){
        return this.provider.searchGraduated(start, end);
    }

    public Student findByName(String name) {
        Optional<Student> os = this.provider.findByName(name);
        if(os.isPresent()) {
            return os.get();
        }
        return null;
    }

    public List<Student> findByNameLike(String name) {
        return this.provider.findByNameLike(name);
    }

    public List<Student> findByGroup(Group group) {
        return this.provider.findByGroup(group);
    }

    public List<Student> findByGrade(int grade) {
        return this.provider.findByGrade(grade);
    }
    
    public Page<Student> findByGraduated(Date graduated, Pageable pageable){
        return this.provider.findByGraduated(graduated, pageable);
    }

    public Page<Student> searchGraduated(Date start, Date end, Pageable pageable){
        return this.provider.searchGraduated(start, end, pageable);
    }

    public Page<Student> findByNameLike(String name, Pageable pageable) {
        return this.provider.findByNameLike(name, pageable);
    }

    public Page<Student> findByGroup(Group group, Pageable pageable) {
        return this.provider.findByGroup(group, pageable);
    }

    public Page<Student> findByGrade(int grade, Pageable pageable) {
        return this.provider.findByGrade(grade, pageable);
    }
    
}
