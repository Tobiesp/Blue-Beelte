package com.tspdevelopment.KidsScore.configure;

import com.tspdevelopment.kidsscore.data.repository.GroupRepository;
import com.tspdevelopment.kidsscore.data.repository.PointCategoryRepository;
import com.tspdevelopment.kidsscore.data.repository.PointTypeRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsEarnedRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.data.repository.RoleRepository;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tobiesp
 */
@Configuration
public class LoadDatabase_test {
    
    @Bean
    CommandLineRunner initDatabase_test(RoleRepository rr, 
                                    UserRepository ur, 
                                    GroupRepository gr, 
                                    StudentRepository sr, 
                                    PointCategoryRepository pcr, 
                                    PointTypeRepository ptr,
                                    PointsEarnedRepository per,
                                    PointsSpentRepository psr,
                                    RunningTotalsRepository rtr) {

        return args -> {
//              TestEntityGenerator.getInstance().setGroupRepository(gr);
//              TestEntityGenerator.getInstance().setPointCategoryRepository(pcr);
//              TestEntityGenerator.getInstance().setPointTotalRepository(ptr);
//              TestEntityGenerator.getInstance().setPointsEarnedRepository(per);
//              TestEntityGenerator.getInstance().setPointsSpentRepository(psr);
//              TestEntityGenerator.getInstance().setRoleRepository(rr);
//              TestEntityGenerator.getInstance().setRunningTotalsRepository(rtr);
//              TestEntityGenerator.getInstance().setStudentRepository(sr);
//              TestEntityGenerator.getInstance().setUserRepository(ur);
//              TestEntityGenerator.getInstance().initiateDatabase();
        };
    }
}
