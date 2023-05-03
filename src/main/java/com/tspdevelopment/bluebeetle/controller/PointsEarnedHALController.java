package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.csv.importmodels.PointsEarnedV1;
import com.tspdevelopment.bluebeetle.data.model.PointsEarned;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.PointTypeRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointsEarnedRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointsEarnedProvider;
import com.tspdevelopment.bluebeetle.response.ImportJobResponse;
import com.tspdevelopment.bluebeetle.response.PointsEarnedCollection;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointsEarnedService;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/api/hal/points/earned")
public class PointsEarnedHALController extends BaseHALController<PointsEarned, PointsEarnedProvider, PointsEarnedService>{
    
    public PointsEarnedHALController(PointsEarnedRepository repository, PointTypeRepository ptRepository, RunningTotalsRepository rtRepository, StudentRepository stdRepository) {
        this.service = new PointsEarnedService(repository, ptRepository, rtRepository, stdRepository);
    }
    
    @GetMapping("/findByStudentAndEvent")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public EntityModel<PointsEarnedCollection> getPointsEarnedByStudentAndEvent(@RequestParam String studentId, @RequestParam String eventDate) {
        if(studentId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing student id.");
        }
        if(eventDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing event date.");
        }
        UUID stdId = UUID.fromString(studentId);
        DateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy");
        LocalDate date;
        try {
            date = dateFormatter.parse(eventDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event date in bad format. Should be in M/d/yyyy: " + eventDate);
        }
        Student std = this.service.getStudent(stdId);
        if(std == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with that id.");
        }
        List<PointsEarned> points = this.service.findByStudentAndEventDate(std, date);
        PointsEarnedCollection pec = new PointsEarnedCollection();
        pec.setEventDate(date);
        pec.setStudent(std);
        pec.setPoints(points);
        return EntityModel.of(pec, 
                linkTo(methodOn(this.getClass()).all()).withSelfRel(), 
                linkTo(methodOn(this.getClass()).search(null)).withSelfRel());
    }
    
    @PostMapping("/addCollection")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public void setPointsEarnedCollection(@RequestBody PointsEarnedCollection collection) {
        if(collection == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing request body.");
        }
        if(collection.getStudent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing student.");
        }
        if(collection.getEventDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing event date.");
        }
        if(collection.getPoints().isEmpty()) {
            return;
        }
        Student student = this.service.getStudent(collection.getStudent().getId());
        LocalDate eventDate = collection.getEventDate();
        for(PointsEarned pe : collection.getPoints()) {
            pe.setStudent(student);
            pe.setEventDate(eventDate);
            this.service.replaceItem(pe, pe.getId());
        }
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Student", "Group", "Grade", "Event Date", "Category", "Points"};
        String[] nameMapping = {"student:name", "student:group:name", "student:grade", "eventDate", "pointCategory:category", "total"};
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
        ImportJobResponse response = this.baseImportCSV(file, PointsEarnedV1.class);
        return ResponseEntity.ok().body(response);
    }
    
}
