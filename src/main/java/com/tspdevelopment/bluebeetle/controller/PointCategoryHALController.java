package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.repository.PointCategoryRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointCategoryProvider;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointCategoryService;
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/hal/category")
public class PointCategoryHALController extends BaseHALController<PointCategory, PointCategoryProvider, PointCategoryService>{

    public PointCategoryHALController(PointCategoryRepository repository) {
        this.service = new PointCategoryService(repository);
        this.AddLinkForList(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) {
        String[] csvHeader = {"Category"};
        String[] nameMapping = {"category"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
}
