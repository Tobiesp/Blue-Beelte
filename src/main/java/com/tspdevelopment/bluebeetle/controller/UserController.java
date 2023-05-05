package com.tspdevelopment.bluebeetle.controller;


import java.util.UUID;

import javax.annotation.security.RolesAllowed;

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
import com.tspdevelopment.bluebeetle.response.UserUpdateView;
import com.tspdevelopment.bluebeetle.services.controllerservice.UserService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/users")
public class UserController extends AdminBaseController<User, UserProvider, UserService>{
    
    public UserController(UserRepository repository, JwtTokenUtil jwtUtillity) {
        this.service = new UserService(repository);
        this.jwtUtillity = jwtUtillity;
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).halFindByUsername(placeHolder)).withRel("findByName"));
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).halFindByEmail(placeHolder)).withRel("fingByEmail"));
        this.AddLinkForList(linkTo(methodOn(this.getClass()).halFindByUsername(placeHolder)).withRel("findByName"));
        this.AddLinkForList(linkTo(methodOn(this.getClass()).halFindByEmail(placeHolder)).withRel("fingByEmail"));
    }
    
    @Override
    @GetMapping(value = "/{id}", produces = { "application/json" })
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public User one(@RequestHeader HttpHeaders headers, @PathVariable UUID id){
        UUID userId = getUserIdFromToken(headers);
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getId().equals(id))) {
            User user = service.getItem(id);
            if(user == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access resource.");
            }
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access resource.");
        }
    }
    
    @PostMapping(value = "/{id}/updatepassword", produces = { "application/json" })
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public User updatePassword(@RequestHeader HttpHeaders headers, 
                                     @PathVariable UUID id, 
                                     @RequestBody UserUpdateView userUpdateView) {
        UUID userId = getUserIdFromToken(headers);
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getId().equals(id))) {
            User user = this.service.updatePassword(id, userUpdateView.getPassword());
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access resource.");
        }
    }
    
    @GetMapping(value = "/findByUsername", produces = { "application/json" })
    @RolesAllowed({Role.ADMIN_ROLE})
    public User findByUsername(@RequestParam Optional<String> name) {
        if(!name.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must be supplied.s");
        }
        User u = this.service.findByUsername(name.get());
        if(u == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return u;
        }
    }
    
    @GetMapping(value = "/findByEmail", produces = { "application/json" })
    @RolesAllowed({Role.ADMIN_ROLE})
    public User findByEmail(@RequestParam Optional<String> email) {
        if(!email.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must be supplied.s");
        }
        User u = this.service.fingByEmail(email.get());
        if(u == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return u;
        }
    }

    @Override
    protected User getUser(UUID id) {
        User u = this.service.getItem(id);
        if(u != null) {
            return u;
        } else {
            throw new ItemNotFound(id);
        }
    }
    
    @GetMapping(value = "/export", produces = { "application/json" })
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Username", "FirstName", "LastName", "email"};
        String[] nameMapping = {"username", "firstName", "lastName", "email"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
    
    
    @Override
    @GetMapping(value = "/{id}", produces = { "application/hal+json" })
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public EntityModel<User> halOne(@RequestHeader HttpHeaders headers, @PathVariable UUID id){
        UUID userId = getUserIdFromToken(headers);
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getId().equals(id))) {
            User user = service.getItem(id);
            if(user == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access resource.");
            }
            return getModelForSingle(user);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access resource.");
        }
    }
    
    @PostMapping(value = "/{id}/updatepassword", produces = { "application/hal+json" })
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public EntityModel<User> halUpdatePassword(@RequestHeader HttpHeaders headers, 
                                     @PathVariable UUID id, 
                                     @RequestBody UserUpdateView userUpdateView)
    {
        return getModelForSingle(this.updatePassword(headers, id, userUpdateView));
    }
    
    @GetMapping(value = "/findByUsername", produces = { "application/hal+json" })
    @RolesAllowed({Role.ADMIN_ROLE})
    public EntityModel<User> halFindByUsername(@RequestParam Optional<String> name) {
        return getModelForSingle(this.findByUsername(name));
    }
    
    @GetMapping(value = "/findByEmail", produces = { "application/hal+json" })
    @RolesAllowed({Role.ADMIN_ROLE})
    public EntityModel<User> halFindByEmail(@RequestParam Optional<String> email) {
        return getModelForSingle(this.findByEmail(email));
    }
    
    @Override
    protected EntityModel<User> getModelForSingle(User c) {
        Link[] list = this.getLinkListForSingle(c.getId());
        Link[] l2 = new Link[list.length+1];
        for(int i=0; i<list.length; i++) {
            if(i == 0) {
                l2[0] = list[i];
                continue;
            } else if(i == 1) {
                l2[1] = linkTo(methodOn(UserController.class).halUpdatePassword(null, c.getId(), null)).withRel("updatePassword");
            }
            l2[i+1] = list[1];
        }
        if(l2[l2.length -1] == null) {
            l2[l2.length -1] = linkTo(methodOn(UserController.class).halUpdatePassword(null, c.getId(), null)).withRel("updatePassword");
        }
        return EntityModel.of(c, l2);
    }
    
    @Override
    protected EntityModel<User> getModelForListItem(User c) {
        Link[] list = this.getLinkListForListItem(c.getId());
        Link[] l2 = new Link[list.length+1];
        for(int i=0; i<list.length; i++) {
            if(i == 0) {
                l2[0] = list[i];
                continue;
            } else if(i == 1) {
                l2[1] = linkTo(methodOn(UserController.class).halUpdatePassword(null, c.getId(), null)).withRel("updatePassword");
            }
            l2[i+1] = list[1];
        }
        if(l2[l2.length -1] == null) {
            l2[l2.length -1] = linkTo(methodOn(UserController.class).halUpdatePassword(null, c.getId(), null)).withRel("updatePassword");
        }
        return EntityModel.of(c, l2);
    }
    
}
