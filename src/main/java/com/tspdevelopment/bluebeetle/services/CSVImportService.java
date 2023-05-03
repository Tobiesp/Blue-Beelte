package com.tspdevelopment.bluebeetle.services;

import com.tspdevelopment.bluebeetle.csv.importmodels.GroupV1;
import com.tspdevelopment.bluebeetle.csv.importmodels.PointsSpentV1;
import com.tspdevelopment.bluebeetle.csv.importmodels.PointsEarnedV1;
import com.tspdevelopment.bluebeetle.csv.importmodels.StudentV1;
import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import com.tspdevelopment.bluebeetle.data.model.PointType;
import com.tspdevelopment.bluebeetle.data.model.PointsEarned;
import com.tspdevelopment.bluebeetle.data.model.PointsSpent;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.GroupRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointCategoryRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointTypeRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointsEarnedRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointsSpentRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.services.controllerservice.GroupService;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointCategoryService;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointTypeService;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointsEarnedService;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointsSpentService;
import com.tspdevelopment.bluebeetle.services.controllerservice.StudentService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 */
@Service
public class CSVImportService {
    
    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(CSVImportService.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    private final GroupService groupService;
    private final PointTypeService pointTypeService;
    private final PointCategoryService pointCategoryService;
    private final PointsEarnedService pointsEarnedService;
    private final PointsSpentService pointsSpentService;
    private final StudentService studentService;

    public CSVImportService(GroupRepository groupRepository,
            PointTypeRepository pointTypeRepository,
            PointsEarnedRepository pointsEarnedRepository,
            PointsSpentRepository pointsSpentRepository,
            PointCategoryRepository pointCategoryRepository,
            RunningTotalsRepository runningTotalsRepository,
            StudentRepository studentRepository) {
        this.groupService = new GroupService(groupRepository);
        this.pointTypeService = new PointTypeService(pointTypeRepository, pointCategoryRepository);
        this.pointCategoryService = new PointCategoryService(pointCategoryRepository);
        this.pointsEarnedService = new PointsEarnedService(pointsEarnedRepository, pointTypeRepository,
                runningTotalsRepository, studentRepository);
        this.pointsSpentService = new PointsSpentService(pointsSpentRepository, runningTotalsRepository);
        this.studentService = new StudentService(studentRepository);
    }
    
    public <K> void importItems(List<K> items, Class<K> clazz) {
        if(clazz == StudentV1.class) {
            importStudents((List<StudentV1>) items);
        } else if(clazz == GroupV1.class) {
            importGroups((List<GroupV1>) items);
        } else if(clazz == PointsEarnedV1.class) {
            importPointsEarned((List<PointsEarnedV1>) items);
        } else if(clazz == PointsSpentV1.class) {
            importPointsSpent((List<PointsSpentV1>) items);
        }
    }
    
    public void importStudents(List<StudentV1> students) {
        for(StudentV1 s: students) {
            Student student = this.studentService.findByName(s.getStudent_name());
            if(student != null) {
                Student std = student;
                std.setGrade(s.getGrade());
                Group group = this.groupService.findByName(s.getGroup());
                if(group != null) {
                    std.setGroup(group);
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Group not found!");
                }
            } else {
                if(s.getStudent_name() == null) {
                    logger.error("Unable to process student: " + String.valueOf(s));
                    continue;
                }
                Student std = new Student();
                std.setName(s.getStudent_name());
                std.setGrade(s.getGrade());
                Group group = this.groupService.findByName(s.getGroup());
                if(group != null) {
                    std.setGroup(group);
                } else {
                    logger.error("Group not found: " + s.getGroup() + " for student: " + s.getStudent_name());
                    continue;
                }
                this.studentService.getNewItem(std);
            }
        }
    }
    
    public void importGroups(List<GroupV1> groups) {
        for(GroupV1 g : groups) {
            Group group = this.groupService.findByName(g.getGroup_name());
            if(group == null) {
                Group grp = new Group();
                grp.setName(g.getGroup_name());
                this.groupService.getNewItem(grp);
            }
        }
    }
    
    public void importPointsEarned(List<PointsEarnedV1> pes) {
        for(PointsEarnedV1 pe : pes) {
            if(pe.getEvent_date() == null) {
                logger.error("Date can't be null");
                continue;
            }
            Student student = this.studentService.findByName(pe.getStudent_name());
            if(student == null) {
                logger.error("Student not found: " + pe.getStudent_name());
                continue;
            }
            if(pe.isAttended()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student);
                setCategory(ptEnd, PointCategory.ATTENDED);
                ptEnd.setTotal(pe.getTotal_points());
                this.pointsEarnedService.getNewItemNoTotalUpdate(ptEnd);
            }
            if(pe.isAttentive()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student);
                setCategory(ptEnd, PointCategory.ATTENTIVE);
                ptEnd.setTotal(0);
                this.pointsEarnedService.getNewItemNoTotalUpdate(ptEnd);
            }
            if(pe.isBible()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student);
                setCategory(ptEnd, PointCategory.BIBLE);
                ptEnd.setTotal(0);
                this.pointsEarnedService.getNewItemNoTotalUpdate(ptEnd);
            }
            if(pe.isBible_verse()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student);
                setCategory(ptEnd, PointCategory.BIBLE_VERSE);
                ptEnd.setTotal(0);
                this.pointsEarnedService.getNewItemNoTotalUpdate(ptEnd);
            }
            if(pe.isBring_a_friend()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student);
                setCategory(ptEnd, PointCategory.BRING_A_FRIEND);
                ptEnd.setTotal(0);
                this.pointsEarnedService.getNewItemNoTotalUpdate(ptEnd);
            }
            if(pe.isRecalls_last_week_lesson()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student);
                setCategory(ptEnd, PointCategory.RECALLS_LAST_WEEK_LESSON);
                ptEnd.setTotal(0);
                this.pointsEarnedService.getNewItemNoTotalUpdate(ptEnd);
            }
        }
    }
    
    private void setCategory(PointsEarned ptEnd, String category) {
        PointCategory pc = this.pointCategoryService.findByCategory(category);
        List<PointType> pt = this.pointTypeService.findByCategoryAndGroup(pc, ptEnd.getStudent().getGroup());
        if(!pt.isEmpty()) {
            ptEnd.setPointCategory(pt.get(0).getPointCategory());
//            ptEnd.setTotal(pt.get(0).getTotalPoints());
        } else {
            logger.error("Point Category not found: " + category);
        }
    }
    
    public void importPointsSpent(List<PointsSpentV1> ps) {
        for(PointsSpentV1 p : ps) {
            if(p.getEvent_date() == null) {
                logger.error("No event date.");
                continue;
            }
            PointsSpent ptEnd = new PointsSpent();
            ptEnd.setEventDate(LocalDate.parse(p.getEvent_date(), formatter));
            Student student = this.studentService.findByName(p.getStudent());
            if(student == null) {
                logger.error("Student not found: " + p.getStudent());
                continue;
            }
            ptEnd.setStudent(student);
            ptEnd.setTotal(p.getPoints_spent());
            this.pointsSpentService.getNewItem(ptEnd);
        }
    }
    
}
