/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.kidsscore.controller;

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
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/import")
public class ImportController {
    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(ImportController.class);

    private GroupProvider groupProvider;
    private PointCategoryProvider pointCategoryProvider;
    private PointsEarnedProvider pointsEarnedProvider;
    private PointsSpentProvider pointsSpentProvider;
    private PointTypeProvider pointTableProvider;
    private RoleProvider roleProvider;
    private UserProvider userProvider;
    private RunningTotalsProvider runningTotalsProvider;
    private StudentProvider studentProvider;
    
    public ImportController(GroupRepository groupRepository,
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
    
    
    @GetMapping("/students")
    @RolesAllowed({Role.ADMIN_ROLE })
    public void CSVImportStudent(HttpServletResponse response) throws IOException {
        
    }
}
