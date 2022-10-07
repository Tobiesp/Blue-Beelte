/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.data.model.PointsSpent;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointsSpentProviderImpl;
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/points/spent")
public class PointsSpentController extends BaseController<PointsSpent>{
    
    public PointsSpentController(PointsSpentRepository repository) {
        this.provider = new PointsSpentProviderImpl(repository);
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public void exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Student", "Group", "Grade", "Event Date", "Points"};
        String[] nameMapping = {"student:name", "student:group:name", "student:grade", "eventDate", "points"};
        this.exportToCSV(response, csvHeader, nameMapping);
    }
    
}
