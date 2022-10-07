package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.data.model.Role;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tspdevelopment.kidsscore.data.model.Student;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.StudentProviderImpl;
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;

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
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public void exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Student", "Group", "Grade"};
        String[] nameMapping = {"name", "group:name", "grade"};
        this.exportToCSV(response, csvHeader, nameMapping);
    }
    
    
}
