/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.kidsscore.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.model.User;
import com.tspdevelopment.kidsscore.data.repository.RoleRepository;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import com.tspdevelopment.kidsscore.exception.ItemNotFound;
import com.tspdevelopment.kidsscore.helpers.JwtTokenUtil;
import com.tspdevelopment.kidsscore.provider.interfaces.RoleProvider;
import com.tspdevelopment.kidsscore.provider.interfaces.UserProvider;
import com.tspdevelopment.kidsscore.provider.sqlprovider.RoleProviderImpl;
import com.tspdevelopment.kidsscore.provider.sqlprovider.UserProviderImpl;


@RestController
@RequestMapping("/api/role")
public class RoleController {
    
    private final JwtTokenUtil jwtUtillity;
    private final RoleProvider provider;
    private final UserProvider userProvider;
    
    RoleController(RoleRepository repository, UserRepository userRepository, JwtTokenUtil jwtUtillity) {
        this.provider = new RoleProviderImpl(repository);
        this.userProvider = new UserProviderImpl(userRepository);
        this.jwtUtillity = jwtUtillity;
    }

    private User getUser(UUID id) {
        Optional<User> u = this.userProvider.findById(id);
        if(u.isPresent()) {
            return u.get();
        } else {
            throw new ItemNotFound(id);
        }
    }

    @GetMapping("/")
    @RolesAllowed({Role.ADMIN_ROLE})
    public CollectionModel<EntityModel<Role>> all() {
        List<EntityModel<Role>> roles = provider.findAll().stream()
				.map(role -> EntityModel.of(role,
						linkTo(methodOn(RoleController.class).one(null, role.getId())).withSelfRel()))
				.collect(Collectors.toList());

		return CollectionModel.of(roles, linkTo(methodOn(RoleController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public EntityModel<Role> one(HttpHeaders headers, UUID id) {
        List<String> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access role.");
        }
        UUID userId = this.jwtUtillity.getUserId(authHeader.get(0));
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getId().equals(id))) {
             Role role = provider.findById(id) //
				.orElseThrow(() -> new ItemNotFound(id));

		return EntityModel.of(role, linkTo(methodOn(UserController.class).one(null, id)).withSelfRel());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access role.");
        }
    }
    
}
