package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.RunningTotals;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.RunningTotalsProvider;
import com.tspdevelopment.bluebeetle.services.controllerservice.RunningTotalsService;
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/points/total/running")
public class RunningTotalsController extends BaseController<RunningTotals, RunningTotalsProvider, RunningTotalsService>{
    
    public RunningTotalsController(RunningTotalsRepository repository) {
        this.service = new RunningTotalsService(repository);
        this.AddLinkForList(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
    }
    
    @GetMapping(value = "/export", produces = { "application/json" })
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) {
        String[] csvHeader = {"Student", "Group", "Grade", "Totals"};
        String[] nameMapping = {"student:name", "student:group:name", "student:grade", "total"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
}
