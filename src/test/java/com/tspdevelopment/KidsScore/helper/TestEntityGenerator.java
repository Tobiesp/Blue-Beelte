/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.KidsScore.helper;

import com.mifmif.common.regex.Generex;
import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.PointCategory;
import com.tspdevelopment.kidsscore.data.model.PointTable;
import com.tspdevelopment.kidsscore.data.model.PointsEarned;
import com.tspdevelopment.kidsscore.data.model.PointsSpent;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.model.RunningTotals;
import com.tspdevelopment.kidsscore.data.model.Student;
import com.tspdevelopment.kidsscore.data.model.User;
import com.tspdevelopment.kidsscore.data.repository.GroupRepository;
import com.tspdevelopment.kidsscore.data.repository.PointCategoryRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsEarnedRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.data.repository.RoleRepository;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.LoggerFactory;
import com.tspdevelopment.kidsscore.data.repository.PointTableRepository;
import java.time.LocalDate;

/**
 *
 * @author tobiesp
 */
public class TestEntityGenerator {
    
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TestEntityGenerator.class);
    private final List<User> userList;
    private UserRepository userRepository;
    private final List<Role> roleList;
    private RoleRepository roleRepository;
    private List<Student> studentList;
    private StudentRepository studentRepository;
    private List<Group> groupList;
    private GroupRepository groupRepository;
    private List<PointTable> ptList;
    private PointTableRepository pointTableRepository;
    private List<PointCategory> pcList;
    private PointCategoryRepository pointCategoryRepository;
    private List<PointsEarned> peList;
    private PointsEarnedRepository pointsEarnedRepository;
    private List<PointsSpent> psList;
    private PointsSpentRepository pointsSpentRepository;
    private List<RunningTotals> rtList;
    private RunningTotalsRepository runningTotalsRepository;
    
    private TestEntityGenerator() {
        this.userList = new ArrayList();
        this.roleList = new ArrayList();
        this.studentList = new ArrayList();
        this.groupList = new ArrayList();
        this.ptList = new ArrayList();
        this.pcList = new ArrayList();
        this.peList = new ArrayList();
        this.psList = new ArrayList();
        this.rtList = new ArrayList();
    }
    
    public void setUserRepository(UserRepository repository) {
        userRepository = repository;
    }
    
    public void setRoleRepository(RoleRepository repository) {
        roleRepository = repository;
    }
    
    public void setGroupRepository(GroupRepository repository) {
        groupRepository = repository;
    }
    
    public void setStudentRepository(StudentRepository repository) {
        studentRepository = repository;
    }
    
    public void setPointTotalRepository(PointTableRepository repository) {
        pointTableRepository = repository;
    }
    
    public void setPointCategoryRepository(PointCategoryRepository repository) {
        pointCategoryRepository = repository;
    }
    
    public void setPointsEarnedRepository(PointsEarnedRepository repository) {
        pointsEarnedRepository = repository;
    }
    
    public void setPointsSpentRepository(PointsSpentRepository repository) {
        pointsSpentRepository = repository;
    }
    
    public void setRunningTotalsRepository(RunningTotalsRepository repository) {
        runningTotalsRepository = repository;
    }
    
    public static TestEntityGenerator getInstance() {
        return TestEntityGeneratorHolder.INSTANCE;
    }
    
    public String generateName() {
        Generex generex = new Generex("[a-zA-Z ]{5,25}");
        return generex.random(5);
    }
    
    public String generatePassword() {
        //Password rules:
        //1. Lenght must be greater then 8
        //2. Must have at least one Captital leter
        //3. Must have at least one number
        //4. Must have at least one of the following symbols: !#$%^&* _-.?
        Generex generex = new Generex("[A-Z][0-9][ !#$%^&*_-.?][a-z][a-zA-Z0-9]{4,20}");
        return generex.random(5);
    }
    
    public String generateUsername() {
        Generex generex = new Generex("[a-zA-Z0-9]{5,25}");
        return generex.random(5);
    }
    
    public Date generateDate() {
        long date = (long) (Math.random() * Long.MAX_VALUE);
        return new Date(date);
    }
    
    public int getRandomInt(int max) {
        return (int) (Math.random() * max);
    }
    
    public double getRandomDouble(double max) {
        return (double) (Math.random() * max);
    }
    
    public boolean generateBoolean(){
        int i = getRandomInt(100);
        return i > 50;
    }
    
    public Role generateRole() {
        Role role;
        if (this.roleList.isEmpty()) {
            role = new Role();
            role.setAuthority(Role.ADMIN_ROLE);
            role = this.roleRepository.save(role);
            this.roleList.add(role);
        } else if (this.roleList.size() == 1) {
            role = new Role(Role.WRITE_ROLE);
            role = this.roleRepository.save(role);
            this.roleList.add(role);
        } else if (this.roleList.size() == 2) {
            role = new Role(Role.READ_ROLE);
            role = this.roleRepository.save(role);
            this.roleList.add(role);
        } else {
            role = this.roleList.get(2);
        }
        return role;
    }
    
    public User generateUser() {
        User user = new User();
        if (this.userList.isEmpty()) {
            user.setUsername("ksAdmin_001");
            user.setFullName("Admin User");
            String pass = generatePassword();
            log.info(String.format("Admin User: ksAdmin_001 \nAdmin Password: %s", pass));
            user.setPassword(pass);
            user.setAuthorities(generateRole());
            log.info(user.toString());
        } else if (this.userList.size() == 1) {
            user.setUsername("ksWriter_001");
            user.setFullName("Writer User");
            String pass = generatePassword();
            log.info(String.format("Writer User: ksAdmin_001 \nWriter Password: %s", pass));
            user.setPassword(pass);
            user.setAuthorities(generateRole());
        } else {
            user.setUsername(generateUsername());
            user.setFullName(generateName());
            String pass = generatePassword();
            user.setPassword(pass);
            user.setAuthorities(generateRole());
        }
        log.info(user.toString());
        user = this.userRepository.save(user);
        this.userList.add(user);
        log.info(user.toString());
        return user;
    }
    
    public Group generateGroup() {
        Group group = new Group();
        if(this.groupList.isEmpty()) {
            group.setName("K-2 Boys");
            group = this.groupRepository.save(group);
            this.groupList.add(group);
        } else if(this.groupList.size() == 1) {
            group.setName("K-2 Girls");
            group = this.groupRepository.save(group);
            this.groupList.add(group);
        } else if(this.groupList.size() == 2) {
            group.setName("3-6 Boys");
            group = this.groupRepository.save(group);
            this.groupList.add(group);
        } else if(this.groupList.size() == 3) {
            group.setName("3-6 Girls");
            group = this.groupRepository.save(group);
            this.groupList.add(group);
        } else if(this.groupList.size() == 4) {
            group.setName("Graduated");
            group = this.groupRepository.save(group);
            this.groupList.add(group);
        } else {
            int rand = (int) (Math.random()*(this.groupList.size()-1));
            group = this.groupList.get(rand);
        }
        return group;
    }
    
    private Student generateStudent() {
        Student student = new Student();
        student.setName(generateName());
        student.setGroup(generateGroup());
        if(student.getGroup().getName().equalsIgnoreCase("K-2 Boys")) {
            int grade = (int) (Math.random()*2);
            student.setGrade(grade);
        } else if(student.getGroup().getName().equalsIgnoreCase("K-2 Girls")) {
            int grade = (int) (Math.random()*2);
            student.setGrade(grade);
        } else if(student.getGroup().getName().equalsIgnoreCase("3-6 Boys")) {
            int grade = (int) (Math.random()*3) + 3;
            student.setGrade(grade);
        } else if(student.getGroup().getName().equalsIgnoreCase("3-6 Girls")) {
            int grade = (int) (Math.random()*3) + 3;
            student.setGrade(grade);
        } else if(student.getGroup().getName().equalsIgnoreCase("Graduated")) {
            int grade = (int) (Math.random()*6) + 6;
            student.setGrade(grade);
            student.setGraduated(new Date());
        }
        student = this.studentRepository.save(student);
        this.studentList.add(student);
        return student;
    }
    
    public PointsEarned generatePointsEarned() {
        PointsEarned pe = new PointsEarned();
        pe.setStudent(generateStudent());
        pe.setPointCategory(generatePointCategory());
        pe.setEventDate(LocalDate.now());
        pe.setTotal(getRandomInt(8));
        pe = this.pointsEarnedRepository.save(pe);
        this.peList.add(pe);
        return pe;
    }
    
    public PointsSpent generatePointsSpent() {
        PointsSpent ps = new PointsSpent();
        ps.setEventDate(LocalDate.now());
        ps.setPoints(this.getRandomInt(20));
        ps.setStudent(generateStudent());
        ps = this.pointsSpentRepository.save(ps);
        this.psList.add(ps);
        return ps;
    }
    
    public RunningTotals generateRunningTotals() {
        RunningTotals rt = new RunningTotals();
        rt.setStudent(this.generateStudent());
        rt.setTotal(this.getRandomInt(50));
        rt = this.runningTotalsRepository.save(rt);
        this.rtList.add(rt);
        return rt;
    }
    
    public PointCategory generatePointCategory() {
        PointCategory pc = new PointCategory();
        if(this.pcList.isEmpty()) {
            pc.setCategory("attended");
            pc = this.pointCategoryRepository.save(pc);
            this.pcList.add(pc);
        } else if(this.pcList.size() == 1) {
            pc.setCategory("bible");
            pc = this.pointCategoryRepository.save(pc);
            this.pcList.add(pc);
        } else if(this.pcList.size() == 2) {
            pc.setCategory("bibleVerse");
            pc = this.pointCategoryRepository.save(pc);
            this.pcList.add(pc);
        } else if(this.pcList.size() == 3) {
            pc.setCategory("bringAFriend");
            pc = this.pointCategoryRepository.save(pc);
            this.pcList.add(pc);
        } else if(this.pcList.size() == 4) {
            pc.setCategory("attentive");
            pc = this.pointCategoryRepository.save(pc);
            this.pcList.add(pc);
        } else if(this.pcList.size() == 5) {
            pc.setCategory("recallsLastWeekLesson");
            pc = this.pointCategoryRepository.save(pc);
            this.pcList.add(pc);
        } else {
            int rand = (int) (Math.random() * (this.pcList.size()-1));
            pc = this.pcList.get(rand);
        }
        return pc;
    }
    
    public PointTable generatePointTable() {
        int rand = (int) (Math.random() * (this.ptList.size()-1));
        return this.ptList.get(rand);
    }
    
    public void initiateDatabase() {
        for(int i=0;i<3;i++) {
            this.generateRole();
        }
        for(int i=0;i<3;i++) {
            this.generateUser();
        }
        for(int i=0;i<5;i++) {
            this.generateGroup();
        }
        for(int i=0;i<7;i++) {
            this.generatePointCategory();
        }
        for (Group g : groupList) {
            for (PointCategory pc : pcList) {
                PointTable pt = new PointTable();
                pt.setGroup(g);
                pt.setPointCategory(pc);
                if(null == g.getName()) {
                    pt.setTotalPoints(0);
                } else switch (g.getName()) {
                    case "Graduated":
                        pt.setTotalPoints(0);
                        break;
                    case "3-6 Boys":
                    case "3-6 Girls":
                        if("attentive".equals(pc.getCategory()) || "recallsLastWeekLesson".equals(pc.getCategory())) {
                            pt.setTotalPoints(0);
                        }   break;
                    case "K-2 Boys":
                    case "K-2 Girls":
                        if("bibleVerse".equals(pc.getCategory())) {
                            pt.setTotalPoints(5);
                        }   break;
                    default:
                        pt.setTotalPoints(1);
                        break;
                }
                this.pointTableRepository.save(pt);
            }
        }
    }
    
    private static class TestEntityGeneratorHolder {

        private static final TestEntityGenerator INSTANCE = new TestEntityGenerator();
    }
}
