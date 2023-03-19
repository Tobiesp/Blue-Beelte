package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.csv.importmodels.StudentV1;
import com.tspdevelopment.kidsscore.data.model.Role;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tspdevelopment.kidsscore.data.model.Student;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.StudentProviderImpl;
import com.tspdevelopment.kidsscore.services.CSVImportService;
import com.tspdevelopment.kidsscore.views.ResponseMessage;
import java.io.IOException;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CSVImportService importService;
    
    
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
    
    
    @GetMapping("/import")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity CSVImport(@RequestParam("file") MultipartFile file) throws IOException {
        return this.CSVImportV1(file);
    }
    
    
    @PostMapping("/import/v1")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity CSVImportV1(@RequestParam("file") MultipartFile file) throws IOException {
        List<StudentV1> results = this.importCSV(file, StudentV1.class);
        importService.importStudents(results);
        String message = "File successfully imported: " + file.getOriginalFilename();
        return ResponseEntity.ok().body(new ResponseMessage(message));
    }
    
    
}
