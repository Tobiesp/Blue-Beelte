package com.tspdevelopment.bluebeetle.controller;


import java.util.Optional;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;

import org.springframework.hateoas.EntityModel;
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
import com.tspdevelopment.bluebeetle.provider.interfaces.UserProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.RoleProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.UserProviderImpl;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/api/hal/role")
public class RoleHALController extends AdminHALBaseController<Role, RoleProvider>{
    
    private final UserProvider userProvider;
    
    public RoleHALController(RoleRepository repository, UserRepository userRepository, JwtTokenUtil jwtUtillity) {
        this.provider = new RoleProviderImpl(repository);
        this.userProvider = new UserProviderImpl(userRepository);
        this.jwtUtillity = jwtUtillity;
    }

    @Override
    @GetMapping("/{id}")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public EntityModel<Role> one(@RequestHeader HttpHeaders headers, @PathVariable UUID id) {
        UUID userId = getUserIdFromToken(headers);
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getRoles().get(0).getId().equals(id))) {
            Role role = provider.findById(id).orElseThrow(() -> new ItemNotFound(id));
            return getModelForSingle(role);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access role.");
        }
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Name"};
        String[] nameMapping = {"authority"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }

    @Override
    protected User getUser(UUID id) {
        Optional<User> u = this.userProvider.findById(id);
        if(u.isPresent()) {
            return u.get();
        } else {
            throw new ItemNotFound(id);
        }
    }
    
}
