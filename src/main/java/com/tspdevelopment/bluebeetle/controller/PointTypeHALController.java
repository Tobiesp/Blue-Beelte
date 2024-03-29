package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.data.model.PointType;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.repository.PointCategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
@RequestMapping("/api/hal/points/config")
public class PointTypeHALController extends BaseHALController<PointType, PointTypeProvider, PointTypeService>{
    
    public PointTypeHALController(PointTypeRepository repository, PointCategoryRepository pointCategoryRepository) {
        this.service = new PointTypeService(repository, pointCategoryRepository);
    }
    
    @GetMapping("/category")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public CollectionModel<EntityModel<PointType>> findByCategory(@RequestParam String category) {
        List<PointType> ptList = ((PointTypeService)this.service).findByCategory(category);
        if(!ptList.isEmpty()) {
            List<EntityModel<PointType>> pthList = ptList.stream()
                .map(c -> this.getModelForList(c))
                .collect(Collectors.toList());

            return CollectionModel.of(pthList, 
                        linkTo(methodOn(PointTypeHALController.class).search(null)).withSelfRel(),
                        linkTo(methodOn(PointTypeHALController.class).findByCategory(null)).withSelfRel(),
                        linkTo(methodOn(PointTypeHALController.class).all()).withSelfRel());
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
