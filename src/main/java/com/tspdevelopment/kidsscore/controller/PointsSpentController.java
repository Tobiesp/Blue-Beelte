package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.csv.importmodels.PointsSpentV1;
import com.tspdevelopment.kidsscore.data.model.PointsSpent;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointsSpentProviderImpl;
import com.tspdevelopment.kidsscore.services.CSVImportService;
import com.tspdevelopment.kidsscore.views.ResponseMessage;
import java.io.IOException;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/points/spent")
public class PointsSpentController extends BaseController<PointsSpent>{
    @Autowired
    private CSVImportService importService;
    
    public PointsSpentController(PointsSpentRepository repository, RunningTotalsRepository rtRepository) {
        this.provider = new PointsSpentProviderImpl(repository, rtRepository);
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Student", "Group", "Grade", "Event Date", "Points"};
        String[] nameMapping = {"student:name", "student:group:name", "student:grade", "eventDate", "points"};
        return this.exportToCSV(response, csvHeader, nameMapping);
    }
    
    @PostMapping("/import")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity CSVImport(@RequestParam("file") MultipartFile file) throws IOException {
        return this.CSVImportV1(file);
    }
    
    @PostMapping("/import/v1")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity CSVImportV1(@RequestParam("file") MultipartFile file) throws IOException {
        List<PointsSpentV1> results = this.importCSV(file, PointsSpentV1.class);
        importService.importPointsSpent(results);
        String message = "File successfully imported: " + file.getOriginalFilename();
        return ResponseEntity.ok().body(new ResponseMessage(message));
    }
    
}
