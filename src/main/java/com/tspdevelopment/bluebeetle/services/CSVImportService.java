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
import com.tspdevelopment.bluebeetle.provider.interfaces.GroupProvider;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointCategoryProvider;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointTypeProvider;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointsEarnedProvider;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointsSpentProvider;
import com.tspdevelopment.bluebeetle.provider.interfaces.StudentProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.GroupProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.PointCategoryProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.PointTypeProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.PointsEarnedProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.PointsSpentProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.StudentProviderImpl;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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
    private final GroupProvider groupProvider;
    private final PointTypeProvider pointTypeProvider;
    private final PointCategoryProvider pointCategoryProvider;
    private final PointsEarnedProvider pointsEarnedProvider;
    private final PointsSpentProvider pointsSpentProvider;
    private final StudentProvider studentProvider;

    public CSVImportService(GroupRepository groupRepository,
            PointTypeRepository pointTypeRepository,
            PointsEarnedRepository pointsEarnedRepository,
            PointsSpentRepository pointsSpentRepository,
            PointCategoryRepository pointCategoryRepository,
            RunningTotalsRepository runningTotalsRepository,
            StudentRepository studentRepository) {
        this.groupProvider = new GroupProviderImpl(groupRepository);
        this.pointTypeProvider = new PointTypeProviderImpl(pointTypeRepository);
        this.pointCategoryProvider = new PointCategoryProviderImpl(pointCategoryRepository);
        this.pointsEarnedProvider = new PointsEarnedProviderImpl(pointsEarnedRepository, pointTypeRepository,
                runningTotalsRepository);
        this.pointsSpentProvider = new PointsSpentProviderImpl(pointsSpentRepository, runningTotalsRepository);
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
                if(s.getStudent_name() == null) {
                    logger.error("Unable to process student: " + String.valueOf(s));
                    continue;
                }
                Student std = new Student();
                std.setName(s.getStudent_name());
                std.setGrade(s.getGrade());
                Optional<Group> group = this.groupProvider.findByName(s.getGroup());
                if(group.isPresent()) {
                    std.setGroup(group.get());
                } else {
                    logger.error("Group not found: " + s.getGroup() + " for student: " + s.getStudent_name());
                    continue;
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
            if(pe.getEvent_date() == null) {
                logger.error("Date can't be null");
                continue;
            }
            Optional<Student> student = ((StudentProviderImpl)this.studentProvider).findByName(pe.getStudent_name());
            if(!student.isPresent()) {
                logger.error("Student not found: " + pe.getStudent_name());
                continue;
            }
            if(pe.isAttended()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student.get());
                setCategory(ptEnd, PointCategory.ATTENDED);
                ptEnd.setTotal(pe.getTotal_points());
                this.pointsEarnedProvider.createNoTotalUpdate(ptEnd);
            }
            if(pe.isAttentive()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student.get());
                setCategory(ptEnd, PointCategory.ATTENTIVE);
                ptEnd.setTotal(0);
                this.pointsEarnedProvider.createNoTotalUpdate(ptEnd);
            }
            if(pe.isBible()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student.get());
                setCategory(ptEnd, PointCategory.BIBLE);
                ptEnd.setTotal(0);
                this.pointsEarnedProvider.createNoTotalUpdate(ptEnd);
            }
            if(pe.isBible_verse()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student.get());
                setCategory(ptEnd, PointCategory.BIBLE_VERSE);
                ptEnd.setTotal(0);
                this.pointsEarnedProvider.createNoTotalUpdate(ptEnd);
            }
            if(pe.isBring_a_friend()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student.get());
                setCategory(ptEnd, PointCategory.BRING_A_FRIEND);
                ptEnd.setTotal(0);
                this.pointsEarnedProvider.createNoTotalUpdate(ptEnd);
            }
            if(pe.isRecalls_last_week_lesson()) {
                PointsEarned ptEnd = new PointsEarned();
                ptEnd.setEventDate(LocalDate.parse(pe.getEvent_date(), formatter));
                ptEnd.setStudent(student.get());
                setCategory(ptEnd, PointCategory.RECALLS_LAST_WEEK_LESSON);
                ptEnd.setTotal(0);
                this.pointsEarnedProvider.createNoTotalUpdate(ptEnd);
            }
        }
    }
    
    private void setCategory(PointsEarned ptEnd, String category) {
        Optional<PointCategory> pc = ((PointCategoryProviderImpl)this.pointCategoryProvider).findByCategory(category);
        List<PointType> pt = ((PointTypeProviderImpl)this.pointTypeProvider).findByCategoryAndGroup(pc.get(), ptEnd.getStudent().getGroup());
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
            Optional<Student> student = ((StudentProviderImpl)this.studentProvider).findByName(p.getStudent());
            if(!student.isPresent()) {
                logger.error("Student not found: " + p.getStudent());
                continue;
            }
            ptEnd.setStudent(student.get());
            ptEnd.setPoints(p.getPoints_spent());
            this.pointsSpentProvider.create(ptEnd);
        }
    }
    
}
