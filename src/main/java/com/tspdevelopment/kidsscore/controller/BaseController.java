package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.data.model.BaseItem;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.provider.interfaces.BaseProvider;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 * @param <T>
 */
public abstract class BaseController<T extends BaseItem> {
    protected BaseProvider<T> provider;
    
    @GetMapping("/")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    CollectionModel<EntityModel<T>> all(){
        List<EntityModel<T>> cList = provider.findAll().stream()
        .map(c -> getModel(c))
        .collect(Collectors.toList());

        return CollectionModel.of(cList, 
                    linkTo(methodOn(StudentController.class).search(null)).withSelfRel(),
                    linkTo(methodOn(StudentController.class).all()).withSelfRel());
    }
    
    @PostMapping("/")
    @RolesAllowed({ Role.WRITE_ROLE, Role.ADMIN_ROLE })
    EntityModel<T> newItem(@RequestBody T newItem){
        return getModel(provider.create(newItem));
    }
    
    @GetMapping("/{id}")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    EntityModel<T> one(@PathVariable UUID id){
        Optional<T> c = provider.findById(id);
        if(c.isPresent()){
            return getModel(c.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }
    
    @PutMapping("/{id}")
    @RolesAllowed({ Role.WRITE_ROLE, Role.ADMIN_ROLE })
    EntityModel<T> replaceItem(@RequestBody T replaceItem, @PathVariable UUID id){
        T c = provider.update(replaceItem, id);
            return getModel(c);
    }
    
    @DeleteMapping("/{id}")
    @RolesAllowed({ Role.WRITE_ROLE, Role.ADMIN_ROLE })
    void deleteItem(@PathVariable UUID id){
        this.provider.delete(id);
    }
    
    @PostMapping("/search")
    @RolesAllowed({ Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    CollectionModel<EntityModel<T>> search(@RequestBody T item){
        List<EntityModel<T>> cList = provider.search(item).stream()
				.map(c -> getModel(c))
				.collect(Collectors.toList());

		return CollectionModel.of(cList, linkTo(methodOn(StudentController.class).all()).withSelfRel());
    }
    
    protected EntityModel<T> getModel(T c) {
        return EntityModel.of(c, //
                linkTo(methodOn(StudentController.class).one(c.getId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).search(null)).withSelfRel(),
                linkTo(methodOn(StudentController.class).all()).withSelfRel());
    }
}
