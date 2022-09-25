/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.model.Student;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.StudentProvider;
import com.tspdevelopment.kidsscore.provider.sqlprovider.StudentProviderImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
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
public class StudentController {
    
    private final StudentProvider provider;

    public StudentController(StudentRepository repository) {
        this.provider = new StudentProviderImpl(repository);
    }
    
    @GetMapping("/")
    @RolesAllowed({Role.ADMIN_ROLE})
    CollectionModel<EntityModel<Student>> all(){
        List<EntityModel<Student>> students = provider.findAll().stream()
        .map(student -> EntityModel.of(student,
                linkTo(methodOn(StudentController.class).one(null, student.getId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).all()).withRel("student")))
        .collect(Collectors.toList());

        return CollectionModel.of(students, 
                    Link.of(linkTo(methodOn(StudentController.class).all()).withRel("student").getHref() + "search", "search"),
                    linkTo(methodOn(StudentController.class).all()).withSelfRel());
    }
    
    @PostMapping("/")
    @RolesAllowed({Role.ADMIN_ROLE})
    Student newItem(@RequestBody Student newItem){
        return provider.create(newItem);
    }
    
    @GetMapping("/{id}")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    EntityModel<Student> one(@RequestHeader HttpHeaders headers, @PathVariable UUID id){
        Optional<Student> student = provider.findById(id);
        if(student.isPresent()){
            return EntityModel.of(student.get(), //
linkTo(methodOn(StudentController.class).one(null, id)).withSelfRel(),
                            Link.of(linkTo(methodOn(StudentController.class).all()).withRel("student").getHref() + "search", "search"),
                            linkTo(methodOn(StudentController.class).all()).withRel("students"));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found");
        }
    }
    
    @PutMapping("/{id}")
    @RolesAllowed({Role.ADMIN_ROLE})
    Student replaceItem(@RequestBody Student replaceItem, @PathVariable UUID id){
        return provider.update(replaceItem, id);
    }
    
    @DeleteMapping("/{id}")
    @RolesAllowed({Role.ADMIN_ROLE})
    void deleteItem(@PathVariable UUID id){
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Can't delete a Student");
    }
    
    @PostMapping("/search")
    @RolesAllowed({Role.ADMIN_ROLE})
    CollectionModel<EntityModel<Student>> search(@RequestBody Student item){
        List<EntityModel<Student>> students = provider.search(item).stream()
				.map(c -> EntityModel.of(c,
						linkTo(methodOn(StudentController.class).one(null, c.getId())).withSelfRel(),
						linkTo(methodOn(StudentController.class).all()).withRel("student")))
				.collect(Collectors.toList());

		return CollectionModel.of(students, linkTo(methodOn(StudentController.class).all()).withSelfRel());
    }
}
