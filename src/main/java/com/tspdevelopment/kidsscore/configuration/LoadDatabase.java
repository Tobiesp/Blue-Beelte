package com.tspdevelopment.kidsscore.configuration;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.PointCategory;
import com.tspdevelopment.kidsscore.data.model.PointTable;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.model.User;
import com.tspdevelopment.kidsscore.data.repository.GroupRepository;
import com.tspdevelopment.kidsscore.data.repository.PointCategoryRepository;
import com.tspdevelopment.kidsscore.data.repository.PointTableRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsEarnedRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.data.repository.RoleRepository;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tobiesp
 */
@Configuration
public class LoadDatabase {
    
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    
    @Bean
    CommandLineRunner initDatabase(RoleRepository rr, 
                                    UserRepository ur, 
                                    GroupRepository gr, 
                                    StudentRepository sr, 
                                    PointCategoryRepository pcr, 
                                    PointTableRepository ptr,
                                    PointsEarnedRepository per,
                                    PointsSpentRepository psr,
                                    RunningTotalsRepository rtr) {

        return args -> {
            createRoles(rr);
            createUsers(ur, rr);
            createGroups(gr);
            createPointCategories(pcr);
            createPointTables(gr, pcr, ptr);
        };
    }
    
    public String generatePassword() {
        //Password rules:
        //1. Lenght must be greater then 8
        //2. Must have at least one Captital leter
        //3. Must have at least one number
        //4. Must have at least one of the following symbols: !#$%^&* _-.?
        StringBuilder sb = new StringBuilder();
        sb.append((char)(((int)(Math.random()*25))+65));
        sb.append((char)(((int)(Math.random()*10))+48));
        sb.append((char)(((int)(Math.random()*25))+97));
        sb.append(" !#$%^&*_-.?".charAt((int)(Math.random()+11)));
        for(int i=0; i<20; i++) {
            int choice = (int)(Math.random()*100);
            if(choice > 0 && choice < 20) {
                sb.append((char)(((int)(Math.random()*25))+65));
            } else if(choice > 20 && choice < 40) {
                sb.append((char)(((int)(Math.random()*10))+48));
            } else if(choice > 40 && choice < 60) {
                sb.append((char)(((int)(Math.random()*25))+97));
            } else if(choice > 60 && choice < 80) {
                sb.append(" !#$%^&*_-.?".charAt((int)(Math.random()+11)));
            }
        }
        return sb.toString();
    }
    
    private void createRoles(RoleRepository rr) {
        Role role_a = new Role(Role.ADMIN_ROLE);
        rr.save(role_a);
        Role role_w = new Role(Role.WRITE_ROLE);
        rr.save(role_w);
        Role role_r = new Role(Role.READ_ROLE);
        rr.save(role_r);
    }
    
    private void createUsers(UserRepository ur, RoleRepository rr) {
        User user_a = new User();
        user_a.setUsername("ksAdmin_001");
        user_a.setFullName("Admin User");
        String pass = generatePassword();
        log.info(String.format("Admin User: %s \nAdmin Password: %s", user_a.getUsername(), pass));
        user_a.setPassword(pass);
        user_a.setAuthorities(rr.findByAuthority(Role.ADMIN_ROLE).get());
        log.info(user_a.toString());
        ur.save(user_a);
        User user_w = new User();
        user_w.setUsername("ksWriter_001");
        user_w.setFullName("Writer User");
        pass = generatePassword();
        log.info(String.format("Writer User: %s \nWriter Password: %s", user_w.getUsername(), pass));
        user_w.setPassword(pass);
        user_w.setAuthorities(rr.findByAuthority(Role.ADMIN_ROLE).get());
        log.info(user_w.toString());
        ur.save(user_w);
        User user_r = new User();
        user_r.setUsername("ksReader_001");
        user_r.setFullName("Reader User");
        pass = generatePassword();
        log.info(String.format("Reader User: %s \nReader Password: %s", user_r.getUsername(), pass));
        user_r.setPassword(pass);
        user_r.setAuthorities(rr.findByAuthority(Role.ADMIN_ROLE).get());
        log.info(user_r.toString());
        ur.save(user_r);
    }
    
    private void createGroups(GroupRepository gr) {
        Group group_kb = new Group();
        group_kb.setName("K-2 Boys");
        gr.save(group_kb);

        Group group_kg = new Group();
        group_kg.setName("K-2 Girls");
        gr.save(group_kg);

        Group group_3b = new Group();
        group_3b.setName("3-6 Boys");
        gr.save(group_3b);

        Group group_3g = new Group();
        group_3g.setName("3-6 Girls");
        gr.save(group_3g);

        Group group_g = new Group();
        group_g.setName("Graduated");
        gr.save(group_g);
    }
    
    private void createPointCategories(PointCategoryRepository pcr) {
        PointCategory pc_at = new PointCategory();
        pc_at.setCategory("attended");
        pcr.save(pc_at);
        
        PointCategory pc_b = new PointCategory();
        pc_b.setCategory("bible");
        pcr.save(pc_b);
        
        PointCategory pc_bv = new PointCategory();
        pc_bv.setCategory("bibleVerse");
        pcr.save(pc_bv);
        
        PointCategory pc_ba = new PointCategory();
        pc_ba.setCategory("bringAFriend");
        pcr.save(pc_ba);
        
        PointCategory pc_av = new PointCategory();
        pc_av.setCategory("attentive");
        pcr.save(pc_av);
        
        PointCategory pc_rl = new PointCategory();
        pc_rl.setCategory("recallsLastWeekLesson");
        pcr.save(pc_rl);
    }
    
    private void createPointTables(GroupRepository gr, PointCategoryRepository pcr, PointTableRepository ptr) {
        List<Group> groups = gr.findAll();
        List<PointCategory> pointCategories = pcr.findAll();
        for (Group g : groups) {
            for (PointCategory pc : pointCategories) {
                PointTable pt = new PointTable();
                pt.setGroup(g);
                pt.setPointCategory(pc);
                if(null == g.getName()) {
                    pt.setTotalPoints(0);
                } else switch (g.getName()) {
                    case "Graduated":
                        pt.setTotalPoints(0);
                        pt.setEnabled(false);
                        break;
                    case "3-6 Boys":
                    case "3-6 Girls":
                        if("attentive".equals(pc.getCategory()) || "recallsLastWeekLesson".equals(pc.getCategory())) {
                            pt.setTotalPoints(0);
                            pt.setEnabled(false);
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
                ptr.save(pt);
            }
        }
    }
}
