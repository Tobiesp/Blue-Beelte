package com.tspdevelopment.kidsscore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tspdevelopment.kidsscore.data.model.Student;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.StudentProviderImpl;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/student")
public class StudentController extends BaseController<Student>{

    public StudentController(StudentRepository repository) {
        this.provider = new StudentProviderImpl(repository);
    }
    
    
}
