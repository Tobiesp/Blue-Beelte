package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.csv.importmodels.PointsEarnedV1;
import com.tspdevelopment.kidsscore.data.model.PointsEarned;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.repository.PointTypeRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsEarnedRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointsEarnedProviderImpl;
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
@RequestMapping("/api/points/earned")
public class PointsEarnedController extends BaseController<PointsEarned>{
    @Autowired
    private CSVImportService importService;
    
    public PointsEarnedController(PointsEarnedRepository repository, PointTypeRepository ptRepository, PointsSpentRepository psRepository, RunningTotalsRepository rtRepository) {
        this.provider = new PointsEarnedProviderImpl(repository, ptRepository, psRepository, rtRepository);
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Student", "Group", "Grade", "Event Date", "Category", "Points"};
        String[] nameMapping = {"student:name", "student:group:name", "student:grade", "eventDate", "pointCategory:category", "total"};
        return this.exportToCSV(response, csvHeader, nameMapping);
    }
    
    @GetMapping("/import")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity CSVImport(@RequestParam("file") MultipartFile file) throws IOException {
        return this.CSVImportV1(file);
    }
    
    @PostMapping("/import/v1")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity CSVImportV1(@RequestParam("file") MultipartFile file) throws IOException {
        List<PointsEarnedV1> results = this.importCSV(file, PointsEarnedV1.class);
        importService.importPointsEarned(results);
        String message = "File successfully imported: " + file.getOriginalFilename();
        return ResponseEntity.ok().body(new ResponseMessage(message));
    }
    
}
