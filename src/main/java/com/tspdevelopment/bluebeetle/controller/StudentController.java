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
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/student")
public class StudentController extends BaseController<Student, StudentProvider, StudentService>{
    
    public StudentController(StudentRepository repository) {
        this.service = new StudentService(repository);
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).halFindByName(placeHolder, placeHolder, placeHolder)).withRel("findByName"));
        this.AddLinkForList(linkTo(methodOn(this.getClass()).halFindByName(placeHolder, placeHolder, placeHolder)).withRel("findByName"));
    }
    
    @GetMapping(value = "/findByName", produces = { "application/json" })
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public List<Student> findByName(@RequestParam Optional<String> name, @RequestParam Optional<String> page, @RequestParam Optional<String> size){
        if(!name.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must be supplied.s");
        }
        List<Student> list;
        if(page.isPresent() && !size.isPresent()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), defaultPageSize);
            Page<Student> p = this.service.findByNameLike(name.get(), pageable);
            list = p.toList();
        } else if(page.isPresent() && size.isPresent()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), Integer.getInteger(size.get(), 10));
            Page<Student> p = this.service.findByNameLike(name.get(), pageable);
            list = p.toList();
        } else {
            list = service.findByNameLike(name.get());
        }
        return list;
    }
    
    @GetMapping(value = "/findByName", produces = { "application/hal+json" })
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public CollectionModel<EntityModel<Student>> halFindByName(@RequestParam Optional<String> name, @RequestParam Optional<String> page, @RequestParam Optional<String> size){
        List<EntityModel<Student>> cList = this.findByName(name, page, size).stream()
        .map(c -> getModelForListItem(c))
        .collect(Collectors.toList());

        return getModelForList(cList);
    }
    
    @GetMapping(value = "/export", produces = { "application/json" })
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
