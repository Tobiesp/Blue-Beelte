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
public class UserController extends AdminBaseController<User>{
    private final JwtTokenUtil jwtUtillity;
    
    UserController(UserRepository repository, JwtTokenUtil jwtUtillity) {
        this.provider = new UserProviderImpl(repository);
        this.jwtUtillity = jwtUtillity;
    }
    
    @Override
    @GetMapping("/{id}")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    EntityModel<User> one(@RequestHeader HttpHeaders headers, @PathVariable UUID id){
        List<String> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access user.");
        }
        UUID userId = this.jwtUtillity.getUserId(authHeader.get(0));
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getId().equals(id))){
            User user = provider.findById(id).orElseThrow();
		    return getModelForSingle(user);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access user.");
        }
    }
    
    @PostMapping("/{id}/updatepassword")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    EntityModel<User> updatePassword(@RequestHeader HttpHeaders headers, 
                                     @PathVariable UUID id, 
                                     @RequestBody UserUpdateView userUpdateView)
    {
        List<String> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access user.");
        }
        UUID userId = this.jwtUtillity.getUserId(authHeader.get(0).split(" ")[1]);
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getId().equals(id))) {
            User user = ((UserProvider) this.provider).updatePassowrd(id, userUpdateView.getPassword());
		    return getModelForSingle(user);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access user.");
        }
    }

    private User getUser(UUID id) {
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
        String[] csvHeader = {"Username", "Full Name"};
        String[] nameMapping = {"username", "fullName"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
    protected EntityModel<User> getModelForSingle(User c) {
        return EntityModel.of(c, //
                linkTo(methodOn(UserController.class).one(null, c.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).updatePassword(null, c.getId(), null)).withSelfRel(),
                linkTo(methodOn(UserController.class).search(null)).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }
    
    protected EntityModel<User> getModelForList(User c) {
        return EntityModel.of(c, //
                linkTo(methodOn(UserController.class).one(null, c.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).updatePassword(null, c.getId(), null)).withSelfRel());
    }
    
}
