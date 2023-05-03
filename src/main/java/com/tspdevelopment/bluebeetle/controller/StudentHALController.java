package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.csv.importmodels.StudentV1;
import com.tspdevelopment.bluebeetle.data.model.Role;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.StudentProvider;
import com.tspdevelopment.bluebeetle.response.ImportJobResponse;
import com.tspdevelopment.bluebeetle.services.controllerservice.StudentService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
@RequestMapping("/api/hal/student")
public class StudentHALController extends BaseHALController<Student, StudentProvider, StudentService>{
    
    public StudentHALController(StudentRepository repository) {
        this.service = new StudentService(repository);
    }
    
    @GetMapping("/findByName")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public CollectionModel<EntityModel<Student>> finByName(@RequestParam String name) throws IOException {
        List<EntityModel<Student>> cList = this.service.findByNameLike(name).stream()
        .map(c -> getModelForList(c))
        .collect(Collectors.toList());

        return CollectionModel.of(cList, 
                    linkTo(methodOn(this.getClass()).search(null)).withSelfRel(),
                    linkTo(methodOn(this.getClass()).all()).withSelfRel());
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Student", "Group", "Grade"};
        String[] nameMapping = {"name", "group:name", "grade"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
    @PostMapping("/import")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> CSVImport(@RequestParam("file") MultipartFile file) throws IOException {
        return this.CSVImportV1(file);
    }
    
    @PostMapping("/import/v1")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> CSVImportV1(@RequestParam("file") MultipartFile file) throws IOException {
        ImportJobResponse response = this.baseImportCSV(file, StudentV1.class);
        return ResponseEntity.ok().body(response);
    }
    
    
}
