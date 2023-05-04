package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.csv.importmodels.PointsSpentV1;
import com.tspdevelopment.bluebeetle.data.model.PointsSpent;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.PointsSpentRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointsSpentProvider;
import com.tspdevelopment.bluebeetle.response.ImportJobResponse;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointsSpentService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/points/spent")
public class PointsSpentController extends BaseController<PointsSpent, PointsSpentProvider, PointsSpentService>{
    
    public PointsSpentController(PointsSpentRepository repository, RunningTotalsRepository rtRepository, StudentRepository stdRepository) {
        this.service = new PointsSpentService(repository, rtRepository, stdRepository);
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Student", "Group", "Grade", "Event Date", "Points"};
        String[] nameMapping = {"student:name", "student:group:name", "student:grade", "eventDate", "points"};
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
        ImportJobResponse response = this.baseImportCSV(file, PointsSpentV1.class);
        return ResponseEntity.ok().body(response);
    }
    
    @GetMapping("/findByEventDate")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public List<PointsSpent> findByEventDate(
            @RequestParam Optional<LocalDate> eventDate, 
            @RequestParam Optional<String> page, 
            @RequestParam Optional<String> size){
        if(eventDate.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must supply an event date");
        }
        List<PointsSpent> list;
        if(page.isPresent() && size.isEmpty()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), defaultPageSize);
            Page<PointsSpent> p = this.service.findByEventDate(eventDate.orElse(null) ,pageable);
            list = p.toList();
        } else if(page.isPresent() && size.isPresent()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), Integer.getInteger(size.get(), 10));
            Page<PointsSpent> p = this.service.findByEventDate(eventDate.orElse(null) ,pageable);
            list = p.toList();
        } else {
            list = service.findByEventDate(eventDate.orElse(null));
        }
        return list;
    }
    
    @GetMapping("/searchEventDate")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public List<PointsSpent> searchEventDate(
            @RequestParam Optional<LocalDate> start, 
            @RequestParam Optional<LocalDate> end, 
            @RequestParam Optional<String> page, 
            @RequestParam Optional<String> size){
        if(start.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must supply an start date");
        }
        if(end.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must supply an end date");
        }
        List<PointsSpent> list;
        if(page.isPresent() && size.isEmpty()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), defaultPageSize);
            Page<PointsSpent> p = this.service.searchEventDate(start.orElse(null), end.orElse(null) ,pageable);
            list = p.toList();
        } else if(page.isPresent() && size.isPresent()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), Integer.getInteger(size.get(), 10));
            Page<PointsSpent> p = this.service.searchEventDate(start.orElse(null), end.orElse(null) ,pageable);
            list = p.toList();
        } else {
            list = service.searchEventDate(start.orElse(null), end.orElse(null));
        }
        return list;
    }
    
    @GetMapping("/findByStudent")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public List<PointsSpent> findByStudent(
            @RequestParam Optional<UUID> studentId, 
            @RequestParam Optional<String> page, 
            @RequestParam Optional<String> size) {
        if(studentId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must supply an student id.");
        }
        Optional<Student> ostd = this.service.getStudent(studentId.get());
        if(ostd.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stduent not found by id: " + studentId.get());
        }
        List<PointsSpent> list;
        if(page.isPresent() && size.isEmpty()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), defaultPageSize);
            Page<PointsSpent> p = this.service.findByStudent(ostd.get(),pageable);
            list = p.toList();
        } else if(page.isPresent() && size.isPresent()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), Integer.getInteger(size.get(), 10));
            Page<PointsSpent> p = this.service.findByStudent(ostd.get(),pageable);
            list = p.toList();
        } else {
            list = service.findByStudent(ostd.get());
        }
        return list;
    }
    
    @GetMapping("/searchStudentEventDate")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public List<PointsSpent> searchStudentEventDate(
            @RequestParam Optional<UUID> studentId, 
            @RequestParam Optional<LocalDate> start, 
            @RequestParam Optional<LocalDate> end, 
            @RequestParam Optional<String> page, 
            @RequestParam Optional<String> size) {
        if(studentId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must supply an student id.");
        }
        Optional<Student> ostd = this.service.getStudent(studentId.get());
        if(ostd.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stduent not found by id: " + studentId.get());
        }
        if(start.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must supply an start date");
        }
        if(end.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must supply an end date");
        }
        List<PointsSpent> list;
        if(page.isPresent() && size.isEmpty()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), defaultPageSize);
            Page<PointsSpent> p = this.service.searchStudentEventDate(ostd.get(), start.orElse(null), end.orElse(null) ,pageable);
            list = p.toList();
        } else if(page.isPresent() && size.isPresent()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), Integer.getInteger(size.get(), 10));
            Page<PointsSpent> p = this.service.searchStudentEventDate(ostd.get(), start.orElse(null), end.orElse(null) ,pageable);
            list = p.toList();
        } else {
            list = service.searchStudentEventDate(ostd.get(), start.orElse(null), end.orElse(null));
        }
        return list;
    }
    
}
