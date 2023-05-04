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
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/points/config")
public class PointTypeController extends BaseController<PointType, PointTypeProvider, PointTypeService>{
    
    public PointTypeController(PointTypeRepository repository, PointCategoryRepository pointCategoryRepository) {
        this.service = new PointTypeService(repository, pointCategoryRepository);
        this.AddLinkForList(linkTo(methodOn(PointTypeController.class).halFindByCategory(null, placeHolder, placeHolder)).withRel("findByCategory"));
        this.AddLinkForSingle(linkTo(methodOn(PointTypeController.class).halFindByCategory(null, placeHolder, placeHolder)).withRel("findByCategory"));
        this.AddLinkForList(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
    }
    
    @GetMapping(value = "/category", produces = { "application/json" })
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public List<PointType> findByCategory(@RequestParam String category, @RequestParam Optional<String> page, @RequestParam Optional<String> size){
        List<PointType> list;
        if(page.isPresent() && !size.isPresent()) {
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
            return list;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        
    }
    
    @GetMapping(value = "/category", produces = { "application/hal+json" })
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public CollectionModel<EntityModel<PointType>> halFindByCategory(@RequestParam String category, @RequestParam Optional<String> page, @RequestParam Optional<String> size){
        List<PointType> list = this.findByCategory(category, page, size);
        if(!list.isEmpty()) {
            List<EntityModel<PointType>> pthList = list.stream()
                .map(c -> this.getModelForListItem(c))
                .collect(Collectors.toList());

            return getModelForList(pthList);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        
    }
    
    @GetMapping(value = "/export", produces = { "application/json" })
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) {
        String[] csvHeader = {"Group", "Category", "points"};
        String[] nameMapping = {"group:name", "pointCategory:category", "totalPoints"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
}
