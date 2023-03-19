/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.kidsscore.services;

import com.tspdevelopment.kidsscore.csv.importmodels.GroupV1;
import com.tspdevelopment.kidsscore.csv.importmodels.PointsEarnedV1;
import com.tspdevelopment.kidsscore.csv.importmodels.StudentV1;
import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.PointCategory;
import com.tspdevelopment.kidsscore.data.model.PointsEarned;
import com.tspdevelopment.kidsscore.data.model.Student;
import com.tspdevelopment.kidsscore.data.repository.GroupRepository;
import com.tspdevelopment.kidsscore.data.repository.PointCategoryRepository;
import com.tspdevelopment.kidsscore.data.repository.PointTypeRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsEarnedRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.data.repository.RoleRepository;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 */
@Service
public class CSVImportService {
    
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private GroupProvider groupProvider;
    private PointCategoryProvider pointCategoryProvider;
    private PointsEarnedProvider pointsEarnedProvider;
    private PointsSpentProvider pointsSpentProvider;
    private PointTypeProvider pointTableProvider;
    private RoleProvider roleProvider;
    private UserProvider userProvider;
    private RunningTotalsProvider runningTotalsProvider;
    private StudentProvider studentProvider;

    public CSVImportService(GroupRepository groupRepository,
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
                pointsSpentProvider, runningTotalsRepository);
        this.pointsSpentProvider = new PointsSpentProviderImpl(pointsSpentProvider);
        this.pointTableProvider = new PointTypeProviderImpl(pointTableRepository);
        this.roleProvider = new RoleProviderImpl(roleRepository);
        this.userProvider = new UserProviderImpl(userRepository);
        this.runningTotalsProvider = new RunningTotalsProviderImpl(runningTotalsRepository);
        this.studentProvider = new StudentProviderImpl(studentRepository);
    }
    
    public void importStudents(List<StudentV1> students) {
        for(StudentV1 s: students) {
            Optional<Student> student = ((StudentProviderImpl)this.studentProvider).findByName(s.getStudent_name());
            if(student.isPresent()) {
                Student std = student.get();
                std.setGrade(s.getGrade());
                Optional<Group> group = this.groupProvider.findByName(s.getGroup());
                if(group.isPresent()) {
                    std.setGroup(group.get());
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Group not found: " + group.get().getName());
                }
            } else {
                Student std = new Student();
                std.setName(s.getStudent_name());
                std.setGrade(s.getGrade());
                Optional<Group> group = this.groupProvider.findByName(s.getGroup());
                if(group.isPresent()) {
                    std.setGroup(group.get());
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Group not found: " + group.get().getName());
                }
                this.studentProvider.create(std);
            }
        }
    }
    
    public void importGroups(List<GroupV1> groups) {
        for(GroupV1 g : groups) {
            Optional<Group> group = ((GroupProviderImpl)this.groupProvider).findByName(g.getGroup_name());
            if(!group.isPresent()) {
                Group grp = new Group();
                grp.setName(g.getGroup_name());
                this.groupProvider.create(grp);
            }
        }
    }
    
    public void importPointsEarned(List<PointsEarnedV1> pes) {
        for(PointsEarnedV1 pe : pes) {
            PointsEarned ptEnd = new PointsEarned();
            ptEnd.setEventDate(LocalDateTime.parse(pe.getEvent_date(), formatter));
            Optional<Student> student = ((StudentProviderImpl)this.studentProvider).findByName(pe.getStudent_name());
            if(!student.isPresent()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Student not found: " + pe.getStudent_name());
            }
            ptEnd.setStudent(student.get());
            if(pe.isAttended()) {
                setCategory(ptEnd, PointCategory.ATTENDED);
            } else if(pe.isAttentive()) {
                setCategory(ptEnd, PointCategory.ATTENTIVE);
            } else if(pe.isBible()) {
                setCategory(ptEnd, PointCategory.BIBLE);
            } else if(pe.isBible_verse()) {
                setCategory(ptEnd, PointCategory.BIBLE_VERSE);
            } else if(pe.isBring_a_friend()) {
                setCategory(ptEnd, PointCategory.BRING_A_FRIEND);
            } else if(pe.isRecalls_last_week_lesson()) {
                setCategory(ptEnd, PointCategory.RECALLS_LAST_WEEK_LESSON);
            }
            ptEnd.setTotal(pe.getTotal_points());
            this.pointsEarnedProvider.create(ptEnd);
        }
    }
    
    private void setCategory(PointsEarned ptEnd, String category) {
        Optional<PointCategory> pc = ((PointCategoryProviderImpl)this.pointCategoryProvider).findByCategory(category);
        if(pc.isPresent()) {
            ptEnd.setPointCategory(pc.get());
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Point Category not found: " + category);
        }
    }
    
}
