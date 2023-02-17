package com.tspdevelopment.kidsscore.controller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.annotation.security.RolesAllowed;

import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.repository.GroupRepository;
import com.tspdevelopment.kidsscore.data.repository.PointCategoryRepository;
import com.tspdevelopment.kidsscore.data.repository.PointTableRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsEarnedRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.data.repository.RoleRepository;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import com.tspdevelopment.kidsscore.pdf.GeneratePDF;
import com.tspdevelopment.kidsscore.provider.interfaces.GroupProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.PointCategoryProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.PointTableProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.PointsEarnedProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.PointsSpentProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.RoleProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.RunningTotalsProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.StudentProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.UserProvider;
import com.tspdevelopment.kidsscore.provider.sqlprovider.GroupProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointCategoryProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointTableProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointsEarnedProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointsSpentProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.RoleProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.RunningTotalsProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.StudentProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.UserProviderImpl;

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
    private PointTableProvider pointTableProvider;
    private RoleProvider roleProvider;
    private UserProvider userProvider;
    private RunningTotalsProvider runningTotalsProvider;
    private StudentProvider studentProvider;

    public ReportController(GroupRepository groupRepository, 
                            PointCategoryRepository pointCategoryRepository,
                            PointsEarnedRepository pointsEarnedRepository,
                            PointsSpentRepository pointsSpentProvider,
                            PointTableRepository pointTableRepository,
                            RoleRepository roleRepository,
                            UserRepository userRepository,
                            RunningTotalsRepository runningTotalsRepository,
                            StudentRepository studentRepository) {
        this.groupProvider = new GroupProviderImpl(groupRepository);
        this.pointCategoryProvider = new PointCategoryProviderImpl(pointCategoryRepository);
        this.pointsEarnedProvider = new PointsEarnedProviderImpl(pointsEarnedRepository, pointTableRepository, pointsSpentProvider, runningTotalsRepository);
        this.pointsSpentProvider = new PointsSpentProviderImpl(pointsSpentProvider);
        this.pointTableProvider = new PointTableProviderImpl(pointTableRepository);
        this.roleProvider = new RoleProviderImpl(roleRepository);
        this.userProvider = new UserProviderImpl(userRepository);
        this.runningTotalsProvider = new RunningTotalsProviderImpl(runningTotalsRepository);
        this.studentProvider = new StudentProviderImpl(studentRepository);
    }

    @GetMapping("/getTestPDF")
    @RolesAllowed({ Role.WRITE_ROLE, Role.ADMIN_ROLE })
    ResponseEntity getTestPDF(){
        try {
            ByteArrayOutputStream pdfStream = GeneratePDF.getInstance().generateTestPDF();
            byte[] contents = pdfStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // Here you have to set the actual filename of your pdf
            String filename = "output.pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            return response;

        } catch (DocumentException e) {
            logger.error("Failed to get PDF Doc", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
