package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.data.model.PointCategory;
import com.tspdevelopment.kidsscore.data.model.PointType;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.repository.PointCategoryRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointTypeProviderImpl;
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
import com.tspdevelopment.kidsscore.data.repository.PointTypeRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.PointCategoryProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.PointTypeProvider;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointCategoryProviderImpl;
import java.io.IOException;
import java.util.Optional;
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
public class PointTypeController extends BaseController<PointType>{
    
    private final PointCategoryProvider pointCategoryProvider;
    
    public PointTypeController(PointTypeRepository repository, PointCategoryRepository pointCategoryRepository) {
        this.provider = new PointTypeProviderImpl(repository);
        this.pointCategoryProvider = new PointCategoryProviderImpl(pointCategoryRepository);
    }
    
    @GetMapping("/category")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    CollectionModel<EntityModel<PointType>> findByCategory(@RequestParam String category) {
        PointTypeProvider prov = (PointTypeProvider)this.provider;
        Optional<PointCategory> pc = pointCategoryProvider.findByCategory(category);
        if(pc.isPresent()) {
            List<EntityModel<PointType>> ptList = prov.findByPointCategory(pc.get()).stream()
                .map(c -> this.getModel(c))
                .collect(Collectors.toList());

            return CollectionModel.of(ptList, 
                        linkTo(methodOn(StudentController.class).search(null)).withSelfRel(),
                        linkTo(methodOn(StudentController.class).all()).withSelfRel());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Group", "Category", "points"};
        String[] nameMapping = {"group:name", "pointCategory:category", "totalPoints"};
        return this.exportToCSV(response, csvHeader, nameMapping);
    }
    
}
