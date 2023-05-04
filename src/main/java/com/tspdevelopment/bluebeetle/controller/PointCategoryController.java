package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.repository.PointCategoryRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointCategoryProvider;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointCategoryService;
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
@RequestMapping("/api/category")
public class PointCategoryController extends BaseController<PointCategory, PointCategoryProvider, PointCategoryService>{

    public PointCategoryController(PointCategoryRepository repository) {
        this.service = new PointCategoryService(repository);
        this.AddLinkForList(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
    }
    
    @GetMapping(value = "/export", produces = { "application/json" })
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) {
        String[] csvHeader = {"Category"};
        String[] nameMapping = {"category"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
}
