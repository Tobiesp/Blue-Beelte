package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.data.model.PointType;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.repository.PointCategoryRepository;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tspdevelopment.bluebeetle.data.repository.PointTypeRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointTypeProvider;
import com.tspdevelopment.bluebeetle.services.controllerservice.PointTypeService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/points/config")
public class PointTypeController extends BaseController<PointType, PointTypeProvider, PointTypeService>{
    
    public PointTypeController(PointTypeRepository repository, PointCategoryRepository pointCategoryRepository) {
        this.service = new PointTypeService(repository, pointCategoryRepository);
    }
    
    @GetMapping("/category")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public List<PointType> findByCategory(@RequestParam String category) {
        List<PointType> ptList = ((PointTypeService)this.service).findByCategory(category);
        if(!ptList.isEmpty()) {
            return ptList;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Group", "Category", "points"};
        String[] nameMapping = {"group:name", "pointCategory:category", "totalPoints"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
}
