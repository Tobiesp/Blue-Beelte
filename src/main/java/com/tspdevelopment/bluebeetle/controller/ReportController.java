package com.tspdevelopment.bluebeetle.controller;


import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.repository.GroupRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointCategoryRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointTypeRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointsEarnedRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointsSpentRepository;
import com.tspdevelopment.bluebeetle.data.repository.RoleRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import com.tspdevelopment.bluebeetle.docs.GenerateReportDocs;
import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.model.PointType;
import com.tspdevelopment.bluebeetle.data.model.PointsEarned;
import com.tspdevelopment.bluebeetle.response.GroupCountView;
import com.tspdevelopment.bluebeetle.response.LastEventView;
import com.tspdevelopment.bluebeetle.services.controllerservice.GroupService;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointCategoryService;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointTypeService;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointsEarnedService;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointsSpentService;
import com.tspdevelopment.bluebeetle.services.controllerservice.RoleService;
import com.tspdevelopment.bluebeetle.services.controllerservice.RunningTotalsService;
import com.tspdevelopment.bluebeetle.services.controllerservice.StudentService;
import com.tspdevelopment.bluebeetle.services.controllerservice.UserService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/report")
public class ReportController {

    protected final org.slf4j.Logger logger = LoggerFactory.getLogger("ReportController");

    private GroupService groupService;
    private PointCategoryService pointCategoryService;
    private PointsEarnedService pointsEarnedService;
    private PointsSpentService pointsSpentService;
    private PointTypeService pointTypeService;
    private RoleService roleService;
    private UserService userService;
    private RunningTotalsService runningTotalsService;
    private StudentService studentService;

    public ReportController(GroupRepository groupRepository,
            PointCategoryRepository pointCategoryRepository,
            PointsEarnedRepository pointsEarnedRepository,
            PointsSpentRepository pointsSpentProvider,
            PointTypeRepository pointTableRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            RunningTotalsRepository runningTotalsRepository,
            StudentRepository studentRepository) {
        this.groupService = new GroupService(groupRepository);
        this.pointCategoryService = new PointCategoryService(pointCategoryRepository);
        this.pointsEarnedService = new PointsEarnedService(pointsEarnedRepository, pointTableRepository,
                runningTotalsRepository, studentRepository);
        this.pointsSpentService = new PointsSpentService(pointsSpentProvider, runningTotalsRepository, studentRepository);
        this.pointTypeService = new PointTypeService(pointTableRepository, pointCategoryRepository);
        this.roleService = new RoleService(roleRepository, userRepository);
        this.userService = new UserService(userRepository);
        this.runningTotalsService = new RunningTotalsService(runningTotalsRepository);
        this.studentService = new StudentService(studentRepository);
    }

    @GetMapping("/getTestHTML")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> getTestHTML() {
        String page = GenerateReportDocs.getInstance().generateTestHTML();
        byte[] contents = page.getBytes();
        HttpHeaders ResponseHeaders = new HttpHeaders();
        ResponseHeaders.setContentType(MediaType.TEXT_HTML);
        ResponseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, ResponseHeaders, HttpStatus.OK);
        return response;
    }

    @GetMapping("/getLastEventSnapshot")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<LastEventView> getLastEventSnapshot() {
        LocalDate EventDate = pointsEarnedService.getLastEventDate();
        LastEventView lew = new LastEventView();
        if(EventDate != null) {
            lew.setEventDate(EventDate);
            List<PointsEarned> points = pointsEarnedService.findByEventDate(EventDate);
            HashMap<String, List<String>> countMap = new HashMap<>();
            int total = 0;
            for(PointsEarned p : points) {
                String g = p.getStudent().getGroup().getName();
                if(countMap.containsKey(g)) {
                    List<String> i = countMap.get(g);
                    if(!i.contains(p.getStudent().getName())) {
                        i.add(p.getStudent().getName());
                        countMap.put(g, i);
                        total = total + 1;
                    }
                } else {
                    if(p.getStudent().getGroup().isGroupActive()) {
                        List<String> i = new ArrayList<>();
                        i.add(p.getStudent().getName());
                        countMap.put(g, i);
                        total = total + 1;
                    }
                }
            }
            for(String key : countMap.keySet()) {
                GroupCountView gcv = new GroupCountView();
                gcv.setGroup(key);
                gcv.setCount(countMap.get(key).size());
                lew.addGroup(gcv);
            }
            lew.setTotal(total);
        }
        HttpHeaders ResponseHeaders = new HttpHeaders();
        ResponseHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<LastEventView> response = new ResponseEntity<>(lew, ResponseHeaders, HttpStatus.OK);
        return response;
    }

    @GetMapping("/checkout")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> getCheckout(@RequestParam String group, @RequestHeader HttpHeaders headers) {
        Group grp = groupService.findByName(group);
        if (grp != null) {
            List<Student> students = studentService.findByGroup(grp);
            HttpHeaders ResponseHeaders = new HttpHeaders();
            if(headers.getAccept().contains(MediaType.APPLICATION_JSON_VALUE)){
                String document = GenerateReportDocs.getInstance().generateCheckoutJSON(students, group);
                byte[] contents = document.getBytes();
                ResponseHeaders.setContentType(MediaType.APPLICATION_JSON);
                ResponseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                ResponseEntity<byte[]> response = new ResponseEntity<>(contents, ResponseHeaders, HttpStatus.OK);
                return response;
            } else {
                String document = GenerateReportDocs.getInstance().generateCheckoutHTML(students, group);
                byte[] contents = document.getBytes();
                ResponseHeaders.setContentType(MediaType.TEXT_HTML);
                ResponseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                ResponseEntity<byte[]> response = new ResponseEntity<>(contents, ResponseHeaders, HttpStatus.OK);
                return response;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Group not found.");
        }

    }

    @GetMapping("/checkin")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> getCheckin(@RequestParam String group, @RequestHeader HttpHeaders headers) {
        Group grp = groupService.findByName(group);
        if (grp != null) {
            List<PointType> points = pointTypeService.findByGroup(grp);
            List<Student> students = studentService.findByGroup(grp);
            HttpHeaders ResponseHeaders = new HttpHeaders();
            if(headers.getAccept().contains(MediaType.APPLICATION_JSON_VALUE)){
                String document = GenerateReportDocs.getInstance().generateCheckinJSON(students, points, group);
                byte[] contents = document.getBytes();
                ResponseHeaders.setContentType(MediaType.APPLICATION_JSON);
                ResponseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                ResponseEntity<byte[]> response = new ResponseEntity<>(contents, ResponseHeaders, HttpStatus.OK);
                return response;
            } else {
                String document = GenerateReportDocs.getInstance().generateCheckinHTML(students, points, group);
                byte[] contents = document.getBytes();
                ResponseHeaders.setContentType(MediaType.TEXT_HTML);
                ResponseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                ResponseEntity<byte[]> response = new ResponseEntity<>(contents, ResponseHeaders, HttpStatus.OK);
                return response;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Group not found.");
        }

    }
}
