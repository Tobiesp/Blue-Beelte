package com.tspdevelopment.bluebeetle.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import com.tspdevelopment.bluebeetle.exception.ItemNotFound;
import com.tspdevelopment.bluebeetle.helpers.JwtTokenUtil;
import com.tspdevelopment.bluebeetle.provider.interfaces.UserProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.UserProviderImpl;
import com.tspdevelopment.bluebeetle.response.UserUpdateView;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/users")
public class UserController extends AdminBaseController<User, UserProvider>{
    
    public UserController(UserRepository repository, JwtTokenUtil jwtUtillity) {
        this.provider = new UserProviderImpl(repository);
        this.jwtUtillity = jwtUtillity;
    }
    
    @Override
    @GetMapping("/{id}")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public EntityModel<User> one(@RequestHeader HttpHeaders headers, @PathVariable UUID id){
        UUID userId = getUserIdFromToken(headers);
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getId().equals(id))) {
            User user = provider.findById(id).orElseThrow();
            return getModelForSingle(user);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access resource.");
        }
    }
    
    @PostMapping("/{id}/updatepassword")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public EntityModel<User> updatePassword(@RequestHeader HttpHeaders headers, 
                                     @PathVariable UUID id, 
                                     @RequestBody UserUpdateView userUpdateView)
    {
        UUID userId = getUserIdFromToken(headers);
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getId().equals(id))) {
            User user = this.provider.updatePassword(id, userUpdateView.getPassword());
            return getModelForSingle(user);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access resource.");
        }
    }

    @Override
    protected User getUser(UUID id) {
        Optional<User> u = this.provider.findById(id);
        if(u.isPresent()) {
            return u.get();
        } else {
            throw new ItemNotFound(id);
        }
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Username", "FirstName", "LastName", "email"};
        String[] nameMapping = {"username", "firstName", "lastName", "email"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
    @Override
    protected EntityModel<User> getModelForSingle(User c) {
        return EntityModel.of(c, //
                linkTo(methodOn(UserController.class).one(null, c.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).updatePassword(null, c.getId(), null)).withSelfRel(),
                linkTo(methodOn(UserController.class).search(null)).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }
    
    @Override
    protected EntityModel<User> getModelForList(User c) {
        return EntityModel.of(c, //
                linkTo(methodOn(UserController.class).one(null, c.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).updatePassword(null, c.getId(), null)).withSelfRel());
    }
    
}
