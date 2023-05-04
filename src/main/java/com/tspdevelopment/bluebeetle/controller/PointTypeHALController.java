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
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        this.AddLinkForList(linkTo(methodOn(PointTypeHALController.class).findByCategory(null, placeHolder, placeHolder)).withRel("findByCategory"));
        this.AddLinkForSingle(linkTo(methodOn(PointTypeHALController.class).findByCategory(null, placeHolder, placeHolder)).withRel("findByCategory"));
        this.AddLinkForList(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
    }
    
    @GetMapping("/category")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public CollectionModel<EntityModel<PointType>> findByCategory(@RequestParam String category, @RequestParam Optional<String> page, @RequestParam Optional<String> size){
        List<PointType> list;
        if(page.isPresent() && size.isEmpty()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), defaultPageSize);
            Page<PointType> p = this.service.findByCategory(category, pageable);
            list = p.toList();
        } else if(page.isPresent() && size.isPresent()) {
            Pageable pageable = PageRequest.of(Integer.getInteger(page.get(), 10), Integer.getInteger(size.get(), 10));
            Page<PointType> p = this.service.findByCategory(category, pageable);
            list = p.toList();
        } else {
            list = service.findByCategory(category);
        }
        if(!list.isEmpty()) {
            List<EntityModel<PointType>> pthList = list.stream()
                .map(c -> this.getModelForListItem(c))
                .collect(Collectors.toList());

            return getModelForList(pthList);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) {
        String[] csvHeader = {"Group", "Category", "points"};
        String[] nameMapping = {"group:name", "pointCategory:category", "totalPoints"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
}
