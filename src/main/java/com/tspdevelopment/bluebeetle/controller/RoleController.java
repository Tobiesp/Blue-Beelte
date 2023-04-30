package com.tspdevelopment.bluebeetle.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
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
import com.tspdevelopment.bluebeetle.provider.interfaces.UserProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.RoleProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.UserProviderImpl;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/role")
public class RoleController extends AdminBaseController<Role>{
    
    private final JwtTokenUtil jwtUtillity;
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

    @Override
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
    
}
