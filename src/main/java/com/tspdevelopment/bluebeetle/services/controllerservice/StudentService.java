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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        Optional<List<Student>> ols = this.provider.findByGroup(group);
        if(ols.isPresent()) {
            return ols.get();
        }
        return new ArrayList<>();
    }

    public List<Student> findByGrade(int grade) {
        Optional<List<Student>> ols = this.provider.findByGrade(grade);
        if(ols.isPresent()) {
            return ols.get();
        }
        return new ArrayList<>();
    }
    
}
