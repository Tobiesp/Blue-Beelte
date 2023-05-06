package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.csv.importmodels.PointsEarnedV1;
import com.tspdevelopment.bluebeetle.data.model.PointsEarned;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.PointTypeRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointsEarnedRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.helpers.TimeHelper;
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
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;

import org.springframework.hateoas.EntityModel;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/points/earned")
public class PointsEarnedController extends BaseController<PointsEarned, PointsEarnedProvider, PointsEarnedService>{
    
    public PointsEarnedController(PointsEarnedRepository repository, PointTypeRepository ptRepository, RunningTotalsRepository rtRepository, StudentRepository stdRepository) {
        this.service = new PointsEarnedService(repository, ptRepository, rtRepository, stdRepository);
        
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).halGetPointsEarnedByStudentAndEvent(placeHolder, placeHolder)).withRel("getCollection"));
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).halGetPossiblePoints(placeHolder)).withRel("getPossiblePoints"));
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).halSetPointsEarnedCollection(null)).withRel("AddCollection"));
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
        
        this.AddLinkForList(linkTo(methodOn(this.getClass()).halSetPointsEarnedCollection(null)).withRel("AddCollection"));
        this.AddLinkForList(linkTo(methodOn(this.getClass()).halGetPointsEarnedByStudentAndEvent(placeHolder, placeHolder)).withRel("getCollection"));
        this.AddLinkForList(linkTo(methodOn(this.getClass()).halGetPossiblePoints(placeHolder)).withRel("getPossiblePoints"));
        this.AddLinkForList(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
    }
    
    @GetMapping(value = "/findByStudentAndEvent", produces = { "application/json" })
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public PointsEarnedCollection getPointsEarnedByStudentAndEvent(@RequestParam Optional<String> studentId, @RequestParam Optional<String> eventDate) {
        if(!studentId.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing student id.");
        }
        if(!eventDate.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing event date.");
        }
        UUID stdId = UUID.fromString(studentId.get());
        DateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy");
        LocalDate date;
        try {
            date = dateFormatter.parse(eventDate.get()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
        return pec;
    }
    
    @GetMapping(value = "/getPossiblePoints", produces = { "application/json" })
    @RolesAllowed({ Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public PointsEarnedCollection getPossiblePoints(@RequestParam Optional<String> studentId) {
        if(!studentId.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing student id.");
        }
        UUID stdId = UUID.fromString(studentId.get());
        Student std = this.service.getStudent(stdId);
        if(std == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with that id.");
        }
        List<PointsEarned> points = this.service.getPossiblePoints(std);
        PointsEarnedCollection pec = new PointsEarnedCollection();
        pec.setEventDate(TimeHelper.getInstance().getPreviousEventDate());
        pec.setStudent(std);
        pec.setPoints(points);
        return pec;
    }
    
    @PostMapping(value = "/addCollection", produces = { "application/json" })
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
    
    @GetMapping(value = "/collection", produces = { "application/hal+json" })
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public EntityModel<PointsEarnedCollection> halGetPointsEarnedByStudentAndEvent(@RequestParam Optional<String> studentId, @RequestParam Optional<String> eventDate) {
        if(!studentId.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing student id.");
        }
        if(!eventDate.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing event date.");
        }
        UUID stdId = UUID.fromString(studentId.get());
        DateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy");
        LocalDate date;
        try {
            date = dateFormatter.parse(eventDate.get()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
        return EntityModel.of(pec, this.getLinkListForSingle(null));
    }
    
    @GetMapping(value = "/getPossiblePoints", produces = { "application/hal+json" })
    @RolesAllowed({ Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public EntityModel<PointsEarnedCollection> halGetPossiblePoints(@RequestParam Optional<String> studentId) {
        if(!studentId.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing student id.");
        }
        UUID stdId = UUID.fromString(studentId.get());
        Student std = this.service.getStudent(stdId);
        if(std == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with that id.");
        }
        List<PointsEarned> points = this.service.getPossiblePoints(std);
        PointsEarnedCollection pec = new PointsEarnedCollection();
        pec.setEventDate(TimeHelper.getInstance().getPreviousEventDate());
        pec.setStudent(std);
        pec.setPoints(points);
        return EntityModel.of(pec, this.getLinkListForSingle(null));
    }
    
    @PostMapping(value = "/collection", produces = { "application/hal+json" })
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> halSetPointsEarnedCollection(@RequestBody PointsEarnedCollection collection) {
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
            return ResponseEntity.accepted().build();
        }
        Student student = this.service.getStudent(collection.getStudent().getId());
        LocalDate eventDate = collection.getEventDate();
        for(PointsEarned pe : collection.getPoints()) {
            pe.setStudent(student);
            pe.setEventDate(eventDate);
            this.service.replaceItem(pe, pe.getId());
        }
        return ResponseEntity.ok().build();
    }
    
    @GetMapping(value = "/export", produces = { "application/json" })
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) {
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
