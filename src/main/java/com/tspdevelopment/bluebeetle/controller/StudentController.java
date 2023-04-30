package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.csv.importmodels.StudentV1;
import com.tspdevelopment.bluebeetle.data.model.Role;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.StudentProviderImpl;
import com.tspdevelopment.bluebeetle.response.ImportJobResponse;
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    
    @PostMapping("/import")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> CSVImport(@RequestParam("file") MultipartFile file) throws IOException {
        return this.CSVImportV1(file);
    }
    
    @PostMapping("/import/v1")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> CSVImportV1(@RequestParam("file") MultipartFile file) throws IOException {
        ImportJobResponse response = this.importCSV(file, StudentV1.class);
        return ResponseEntity.ok().body(response);
    }
    
    
}
