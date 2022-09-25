/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.repository.GroupRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.GroupProvider;
import com.tspdevelopment.kidsscore.provider.sqlprovider.GroupProviderImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/group")
public class GroupController {
    
    private final GroupProvider provider;

    public GroupController(GroupRepository repository) {
        this.provider = new GroupProviderImpl(repository);
    }
    
    @GetMapping("/")
    @RolesAllowed({Role.ADMIN_ROLE})
    CollectionModel<EntityModel<Group>> all(){
        List<EntityModel<Group>> groups = provider.findAll().stream()
        .map(group -> EntityModel.of(group,
                linkTo(methodOn(GroupController.class).one(null, group.getId())).withSelfRel(),
                linkTo(methodOn(GroupController.class).all()).withRel("group")))
        .collect(Collectors.toList());

        return CollectionModel.of(groups, 
                    Link.of(linkTo(methodOn(GroupController.class).all()).withRel("group").getHref() + "search", "search"),
                    linkTo(methodOn(GroupController.class).all()).withSelfRel());
    }
    
    @PostMapping("/")
    @RolesAllowed({Role.ADMIN_ROLE})
    Group newItem(@RequestBody Group newItem){
        return provider.create(newItem);
    }
    
    @GetMapping("/{id}")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    EntityModel<Group> one(@RequestHeader HttpHeaders headers, @PathVariable UUID id){
        Optional<Group> group = provider.findById(id);
        if(group.isPresent()){
            return EntityModel.of(group.get(), //
                            linkTo(methodOn(GroupController.class).one(null, id)).withSelfRel(),
                            Link.of(linkTo(methodOn(GroupController.class).all()).withRel("group").getHref() + "search", "search"),
                            linkTo(methodOn(GroupController.class).all()).withRel("groups"));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }
    }
    
    @PutMapping("/{id}")
    @RolesAllowed({Role.ADMIN_ROLE})
    Group replaceItem(@RequestBody Group replaceItem, @PathVariable UUID id){
        return provider.update(replaceItem, id);
    }
    
    @DeleteMapping("/{id}")
    @RolesAllowed({Role.ADMIN_ROLE})
    void deleteItem(@PathVariable UUID id){
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Can't delete a Group");
    }
    
    @PostMapping("/search")
    @RolesAllowed({Role.ADMIN_ROLE})
    CollectionModel<EntityModel<Group>> search(@RequestBody Group item){
        List<EntityModel<Group>> companies = provider.search(item).stream()
				.map(c -> EntityModel.of(c,
						linkTo(methodOn(GroupController.class).one(null, c.getId())).withSelfRel(),
						linkTo(methodOn(GroupController.class).all()).withRel("group")))
				.collect(Collectors.toList());

		return CollectionModel.of(companies, linkTo(methodOn(GroupController.class).all()).withSelfRel());
    }
}
