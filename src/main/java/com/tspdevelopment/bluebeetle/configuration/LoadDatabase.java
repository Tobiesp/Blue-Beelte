package com.tspdevelopment.bluebeetle.configuration;

import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import com.tspdevelopment.bluebeetle.data.model.PointType;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.data.repository.GroupRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointCategoryRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointTypeRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointsEarnedRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointsSpentRepository;
import com.tspdevelopment.bluebeetle.data.repository.RoleRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
            PointTypeRepository ptr,
            PointsEarnedRepository per,
            PointsSpentRepository psr,
            RunningTotalsRepository rtr) {

        return args -> {
            createRoles(rr);
            createUsers(ur, rr);
            createGroups(gr);
            createPointCategories(pcr);
            createPointTypeTables(gr, pcr, ptr);
        };
    }

    public String generatePassword() {
        // Password rules:
        // 1. Lenght must be greater then 8
        // 2. Must have at least one Captital leter
        // 3. Must have at least one number
        // 4. Must have at least one of the following symbols: !#$%^&* _-.?
        StringBuilder sb = new StringBuilder();
        sb.append((char) (((int) (Math.random() * 25)) + 65));
        sb.append((char) (((int) (Math.random() * 10)) + 48));
        sb.append((char) (((int) (Math.random() * 25)) + 97));
        sb.append(" !#$%^&*_-.?".charAt((int) (Math.random() + 11)));
        for (int i = 0; i < 20; i++) {
            int choice = (int) (Math.random() * 100);
            if (choice > 0 && choice < 20) {
                sb.append((char) (((int) (Math.random() * 25)) + 65));
            } else if (choice > 20 && choice < 40) {
                sb.append((char) (((int) (Math.random() * 10)) + 48));
            } else if (choice > 40 && choice < 60) {
                sb.append((char) (((int) (Math.random() * 25)) + 97));
            } else if (choice > 60 && choice < 80) {
                sb.append(" !#$%^&*_-.?".charAt((int) (Math.random() + 11)));
            }
        }
        return sb.toString();
    }

    private void createRoles(RoleRepository rr) {
        if (!hasRole(rr, Role.ADMIN_ROLE))
            rr.save(new Role(Role.ADMIN_ROLE));
        if (!hasRole(rr, Role.WRITE_ROLE))
            rr.save(new Role(Role.WRITE_ROLE));
        if (!hasRole(rr, Role.READ_ROLE))
            rr.save(new Role(Role.READ_ROLE));
        if (!hasRole(rr, Role.NO_ROLE))
            rr.save(new Role(Role.NO_ROLE));
        rr.flush();
    }

    private boolean hasRole(RoleRepository rr, String role) {
        Optional<Role> r = rr.findByAuthority(role);
        return r.isPresent();
    }

    private void createUsers(UserRepository ur, RoleRepository rr) {
        if (!ur.findByUsername("ksAdmin").isPresent()) {
            User user_a = new User();
            user_a.setUsername("ksAdmin");
            user_a.setFullName("Admin User");
            String pass = "Admin_Passw0rd!";
            log.info(String.format("Admin User: %s \nAdmin Password: %s", user_a.getUsername(), pass));
            user_a.setPassword(pass);
            user_a.setAuthorities(rr.findByAuthority(Role.ADMIN_ROLE).get());
            user_a.setCreatedAt(LocalDateTime.now());
            user_a.setModifiedAt(LocalDateTime.now());
            log.info(user_a.toString());
            ur.save(user_a);
            ur.flush();
        }
    }

    private void createGroups(GroupRepository gr) {
        saveGroup(gr, "K-2 Boys");
        saveGroup(gr, "K-2 Girls");
        saveGroup(gr, "3-6 Boys");
        saveGroup(gr, "3-6 Girls");
        saveGroup(gr, "Graduated");
        gr.flush();
    }

    private void saveGroup(GroupRepository gr, String name) {
        if (!hasGroup(gr, name)) {
            gr.save(createGroup(name));
        }
    }

    private boolean hasGroup(GroupRepository gr, String name) {
        return gr.findByName(name).isPresent();
    }

    private Group createGroup(String name) {
        Group grp = new Group();
        grp.setName(name);
        grp.setCreatedAt(LocalDateTime.now());
        return grp;
    }

    private void createPointCategories(PointCategoryRepository pcr) {
        if (!hasPointCategroy(pcr, PointCategory.ATTENDED))
            pcr.save(createPointCategory(PointCategory.ATTENDED));

        if (!hasPointCategroy(pcr, PointCategory.BIBLE))
            pcr.save(createPointCategory(PointCategory.BIBLE));

        if (!hasPointCategroy(pcr, PointCategory.BIBLE_VERSE))
            pcr.save(createPointCategory(PointCategory.BIBLE_VERSE));

        if (!hasPointCategroy(pcr, PointCategory.BRING_A_FRIEND))
            pcr.save(createPointCategory(PointCategory.BRING_A_FRIEND));

        if (!hasPointCategroy(pcr, PointCategory.ATTENTIVE))
            pcr.save(createPointCategory(PointCategory.ATTENTIVE));

        if (!hasPointCategroy(pcr, PointCategory.RECALLS_LAST_WEEK_LESSON))
            pcr.save(createPointCategory(PointCategory.RECALLS_LAST_WEEK_LESSON));
        pcr.flush();
    }

    private boolean hasPointCategroy(PointCategoryRepository pcr, String category) {
        return pcr.findByCategory(category).isPresent();
    }

    private PointCategory createPointCategory(String category) {
        PointCategory pc = new PointCategory();
        pc.setCategory(category);
        return pc;
    }

    private void createPointTypeTables(GroupRepository gr, PointCategoryRepository pcr, PointTypeRepository ptr) {
        List<Group> groups = gr.findAll();
        List<PointCategory> pointCategories = pcr.findAll();
        for (Group g : groups) {
            for (PointCategory pc : pointCategories) {
                if (!hasPointType(ptr, g, pc)) {
                    PointType pt = new PointType();
                    pt.setGroup(g);
                    pt.setPointCategory(pc);
                    if (null == g.getName()) {
                        pt.setTotalPoints(0);
                    } else
                        switch (g.getName()) {
                            case "Graduated":
                                pt.setTotalPoints(0);
                                pt.setEnabled(false);
                                break;
                            case "3-6 Boys":
                            case "3-6 Girls":
                                if ("attentive".equals(pc.getCategory())
                                        || "recallsLastWeekLesson".equals(pc.getCategory())) {
                                    pt.setTotalPoints(0);
                                    pt.setEnabled(false);
                                    break;
                                }
                            case "K-2 Boys":
                            case "K-2 Girls":
                                if ("bibleVerse".equals(pc.getCategory())) {
                                    pt.setTotalPoints(5);
                                    break;
                                }
                            default:
                                pt.setTotalPoints(1);
                                break;
                        }
                    ptr.save(pt);
                }
            }
        }
        gr.flush();
        pcr.flush();
        ptr.flush();
    }

    private boolean hasPointType(PointTypeRepository ptr, Group g, PointCategory pc) {
        List<PointType> list = ptr.findByGroup(g);
        for (PointType ptt : list) {
            if (ptt.getPointCategory().equals(pc)) {
                return true;
            }
        }
        return false;
    }
}
