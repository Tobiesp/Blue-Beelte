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
public class UserController extends AdminBaseController<User, UserProvider, UserService>{
    
    public UserController(UserRepository repository, JwtTokenUtil jwtUtillity) {
        this.service = new UserService(repository);
        this.jwtUtillity = jwtUtillity;
    }
    
    @Override
    @GetMapping("/{id}")
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
    
    @PostMapping("/{id}/updatepassword")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE})
    public User updatePassword(@RequestHeader HttpHeaders headers, 
                                     @PathVariable UUID id, 
                                     @RequestBody UserUpdateView userUpdateView)
    {
        UUID userId = getUserIdFromToken(headers);
        User u = getUser(userId);
        if((u.getAuthorities().contains(new Role(Role.ADMIN_ROLE))) || (u.getId().equals(id))) {
            User user = this.service.updatePassword(id, userUpdateView.getPassword());
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to access resource.");
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
    
    @GetMapping("/export")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Username", "FirstName", "LastName", "email"};
        String[] nameMapping = {"username", "firstName", "lastName", "email"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
}
