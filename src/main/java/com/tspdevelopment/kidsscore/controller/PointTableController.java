/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.data.model.PointCategory;
import com.tspdevelopment.kidsscore.data.model.PointTable;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.repository.PointCategoryRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointTableProviderImpl;
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
import com.tspdevelopment.kidsscore.data.repository.PointTableRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.PointCategoryProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.PointTableProvider;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointCategoryProviderImpl;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/points/config")
public class PointTableController extends BaseController<PointTable>{
    
    private final PointCategoryProvider pointCategoryProvider;
    
    public PointTableController(PointTableRepository repository, PointCategoryRepository pointCategoryRepository) {
        this.provider = new PointTableProviderImpl(repository);
        this.pointCategoryProvider = new PointCategoryProviderImpl(pointCategoryRepository);
    }
    
    @GetMapping("category/")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    CollectionModel<EntityModel<PointTable>> findByCategory(@RequestParam String category) {
        PointTableProvider prov = (PointTableProvider)this.provider;
        Optional<PointCategory> pc = pointCategoryProvider.findByCategory(category);
        if(pc.isPresent()) {
            List<EntityModel<PointTable>> ptList = prov.findByPointCategory(pc.get()).stream()
                .map(c -> this.getModel(c))
                .collect(Collectors.toList());

            return CollectionModel.of(ptList, 
                        linkTo(methodOn(StudentController.class).search(null)).withSelfRel(),
                        linkTo(methodOn(StudentController.class).all()).withSelfRel());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        
    }
    
}
