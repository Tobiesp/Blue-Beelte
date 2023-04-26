package com.tspdevelopment.kidsscore.controller;


import java.util.Optional;
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

import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.repository.GroupRepository;
import com.tspdevelopment.kidsscore.data.repository.PointCategoryRepository;
import com.tspdevelopment.kidsscore.data.repository.PointTypeRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsEarnedRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.data.repository.RoleRepository;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import com.tspdevelopment.kidsscore.docs.GenerateReportDocs;
import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.Student;
import com.tspdevelopment.kidsscore.data.model.PointType;
import com.tspdevelopment.kidsscore.data.model.PointsEarned;
import com.tspdevelopment.kidsscore.provider.interfaces.GroupProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.PointCategoryProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.PointTypeProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.PointsEarnedProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.PointsSpentProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.RoleProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.RunningTotalsProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.StudentProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.UserProvider;
import com.tspdevelopment.kidsscore.provider.sqlprovider.GroupProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointCategoryProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointTypeProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointsEarnedProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointsSpentProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.RoleProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.RunningTotalsProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.StudentProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.UserProviderImpl;
import com.tspdevelopment.kidsscore.views.GroupCountView;
import com.tspdevelopment.kidsscore.views.LastEventView;
import java.time.LocalDate;
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

    private GroupProvider groupProvider;
    private PointCategoryProvider pointCategoryProvider;
    private PointsEarnedProvider pointsEarnedProvider;
    private PointsSpentProvider pointsSpentProvider;
    private PointTypeProvider pointTableProvider;
    private RoleProvider roleProvider;
    private UserProvider userProvider;
    private RunningTotalsProvider runningTotalsProvider;
    private StudentProvider studentProvider;

    public ReportController(GroupRepository groupRepository,
            PointCategoryRepository pointCategoryRepository,
            PointsEarnedRepository pointsEarnedRepository,
            PointsSpentRepository pointsSpentProvider,
            PointTypeRepository pointTableRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            RunningTotalsRepository runningTotalsRepository,
            StudentRepository studentRepository) {
        this.groupProvider = new GroupProviderImpl(groupRepository);
        this.pointCategoryProvider = new PointCategoryProviderImpl(pointCategoryRepository);
        this.pointsEarnedProvider = new PointsEarnedProviderImpl(pointsEarnedRepository, pointTableRepository,
                runningTotalsRepository);
        this.pointsSpentProvider = new PointsSpentProviderImpl(pointsSpentProvider, runningTotalsRepository);
        this.pointTableProvider = new PointTypeProviderImpl(pointTableRepository);
        this.roleProvider = new RoleProviderImpl(roleRepository);
        this.userProvider = new UserProviderImpl(userRepository);
        this.runningTotalsProvider = new RunningTotalsProviderImpl(runningTotalsRepository);
        this.studentProvider = new StudentProviderImpl(studentRepository);
    }

    @GetMapping("/getTestHTML")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    ResponseEntity<?> getTestHTML() {
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
    ResponseEntity<LastEventView> getLastEventSnapshot() {
        LocalDate EventDate = pointsEarnedProvider.getLastEventDate();
        LastEventView lew = new LastEventView();
        if(EventDate != null) {
            lew.setEventDate(EventDate);
            List<PointsEarned> points = pointsEarnedProvider.findByEventDate(EventDate);
            HashMap<String, Integer> countMap = new HashMap<>();
            int total = 0;
            for(PointsEarned p : points) {
                String g = p.getStudent().getGroup().getName();
                if(countMap.containsKey(g)) {
                    Integer i = countMap.get(g);
                    countMap.put(g, i+1);
                    total = total + 1;
                } else {
                    if(!"Graduated".equals(g)) {
                        countMap.put(g, 1);
                        total = total + 1;
                    }
                }
            }
            for(String key : countMap.keySet()) {
                GroupCountView gcv = new GroupCountView();
                gcv.setGroup(key);
                gcv.setCount(countMap.get(key));
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
    ResponseEntity<?> getCheckout(@RequestParam String group, @RequestHeader HttpHeaders headers) {
        Optional<Group> grp = groupProvider.findByName(group);
        if (grp.isPresent()) {
            Optional<List<Student>> students = studentProvider.findByGroup(grp.get());
            HttpHeaders ResponseHeaders = new HttpHeaders();
            if(headers.getAccept().contains(MediaType.APPLICATION_JSON_VALUE)){
                String document = GenerateReportDocs.getInstance().generateCheckoutJSON(students.get(), group);
                byte[] contents = document.getBytes();
                ResponseHeaders.setContentType(MediaType.APPLICATION_JSON);
                ResponseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                ResponseEntity<byte[]> response = new ResponseEntity<>(contents, ResponseHeaders, HttpStatus.OK);
                return response;
            } else {
                String document = GenerateReportDocs.getInstance().generateCheckoutHTML(students.get(), group);
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
    ResponseEntity<?> getCheckin(@RequestParam String group, @RequestHeader HttpHeaders headers) {
        Optional<Group> grp = groupProvider.findByName(group);
        if (grp.isPresent()) {
            List<PointType> points = pointTableProvider.findByGroup(grp.get());
            Optional<List<Student>> students = studentProvider.findByGroup(grp.get());
            HttpHeaders ResponseHeaders = new HttpHeaders();
            if(headers.getAccept().contains(MediaType.APPLICATION_JSON_VALUE)){
                String document = GenerateReportDocs.getInstance().generateCheckinJSON(students.get(), points, group);
                byte[] contents = document.getBytes();
                ResponseHeaders.setContentType(MediaType.APPLICATION_JSON);
                ResponseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                ResponseEntity<byte[]> response = new ResponseEntity<>(contents, ResponseHeaders, HttpStatus.OK);
                return response;
            } else {
                String document = GenerateReportDocs.getInstance().generateCheckinHTML(students.get(), points, group);
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
