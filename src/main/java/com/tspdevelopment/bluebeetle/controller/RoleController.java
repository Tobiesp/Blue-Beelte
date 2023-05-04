package com.tspdevelopment.bluebeetle.controller;


import java.util.UUID;

import javax.annotation.security.RolesAllowed;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.data.repository.RoleRepository;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import com.tspdevelopment.bluebeetle.exception.ItemNotFound;
import com.tspdevelopment.bluebeetle.helpers.JwtTokenUtil;
import com.tspdevelopment.bluebeetle.provider.interfaces.RoleProvider;
import com.tspdevelopment.bluebeetle.services.controllerservice.RoleService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/api/role")
public class RoleController extends AdminBaseController<Role, RoleProvider, RoleService>{
    
    public RoleController(RoleRepository repository, UserRepository userRepository, JwtTokenUtil jwtUtillity) {
        this.service = new RoleService(repository, userRepository);
        this.jwtUtillity = jwtUtillity;
        this.AddLinkForList(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).exportToCSV(null)).withRel("export"));
    }

    @Override
    @GetMapping(value = "/{id}", produces = { "application/json" })
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public Role one(@RequestHeader HttpHeaders headers, @PathVariable UUID id) {
        UUID userId = getUserIdFromToken(headers);
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getUserRole().getId().equals(id))) {
            Role role = service.getItem(id);
            if(role == null) {
                throw new ItemNotFound(id);
            }
            return role;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access role.");
        }
    }
    
    @GetMapping(value = "/export", produces = { "application/json" })
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) {
        String[] csvHeader = {"Name"};
        String[] nameMapping = {"authority"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }

    @Override
    @GetMapping(value = "/{id}", produces = { "application/hal+json" })
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public EntityModel<Role> halOne(@RequestHeader HttpHeaders headers, @PathVariable UUID id) {
        UUID userId = getUserIdFromToken(headers);
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getUserRole().getId().equals(id))) {
            Role role = service.getItem(id);
            if(role == null) {
                throw new ItemNotFound(id);
            }
            return getModelForSingle(role);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access role.");
        }
    }

    @Override
    protected User getUser(UUID id) {
        User u = this.service.getUserById(id);
        if(u != null) {
            return u;
        } else {
            throw new ItemNotFound(id);
        }
    }
    
}
