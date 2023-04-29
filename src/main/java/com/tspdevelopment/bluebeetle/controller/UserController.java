package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.csv.CSVPreference;
import com.tspdevelopment.bluebeetle.csv.CSVWriter;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class UserController {
    
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final JwtTokenUtil jwtUtillity;
    private final UserProvider provider;
    
    UserController(UserRepository repository, JwtTokenUtil jwtUtillity) {
        this.provider = new UserProviderImpl(repository);
        this.jwtUtillity = jwtUtillity;
    }
    
    @GetMapping("/")
    @RolesAllowed({Role.ADMIN_ROLE})
    CollectionModel<EntityModel<User>> all(){
        List<EntityModel<User>> users = provider.findAll().stream()
        .map(user -> EntityModel.of(user,
                linkTo(methodOn(UserController.class).one(null, user.getId())).withSelfRel(),
                                        linkTo(methodOn(UserController.class).updatePassword(null, user.getId(), null)).withRel("updatepassword"),
                linkTo(methodOn(UserController.class).all()).withRel("user")))
        .collect(Collectors.toList());

        return CollectionModel.of(users, 
                    Link.of(linkTo(methodOn(UserController.class).all()).withRel("user").getHref() + "search", "search"),
                    linkTo(methodOn(UserController.class).all()).withSelfRel());
    }
    
    @PostMapping("/")
    @RolesAllowed({Role.ADMIN_ROLE})
    User newItem(@RequestBody User newItem){
        String s = String.format("New User Password: %s", newItem.getPassword());
        log.info(s);
        return provider.create(newItem);
    }
    
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
             User user = provider.findById(id) //
				.orElseThrow();

		return EntityModel.of(user, //
				linkTo(methodOn(UserController.class).one(null, id)).withSelfRel(),
                                linkTo(methodOn(UserController.class).updatePassword(null, user.getId(), null)).withRel("updatepassword"),
                                Link.of(linkTo(methodOn(UserController.class).all()).withRel("user").getHref() + "search", "search"),
				linkTo(methodOn(UserController.class).all()).withRel("users"));
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
             User user = this.provider.updatePassowrd(id, userUpdateView.getPassword());
		return EntityModel.of(user, //
				linkTo(methodOn(UserController.class).one(null, id)).withSelfRel(),
                                Link.of(linkTo(methodOn(UserController.class).all()).withRel("user").getHref() + "search", "search"),
                                linkTo(methodOn(UserController.class).updatePassword(null, user.getId(), null)).withRel("updatepassword"),
				linkTo(methodOn(UserController.class).all()).withRel("users"));
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
    
    @PutMapping("/{id}")
    @RolesAllowed({Role.ADMIN_ROLE})
    User replaceItem(@RequestBody User replaceItem, @PathVariable UUID id){
        return provider.update(replaceItem, id);
    }
    
    @DeleteMapping("/{id}")
    @RolesAllowed({Role.ADMIN_ROLE})
    ResponseEntity deleteItem(@PathVariable UUID id){
        this.provider.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/search")
    @RolesAllowed({Role.ADMIN_ROLE})
    CollectionModel<EntityModel<User>> search(@RequestBody User item){
        List<EntityModel<User>> companies = provider.search(item).stream()
				.map(c -> EntityModel.of(c,
						linkTo(methodOn(UserController.class).one(null, c.getId())).withSelfRel(),
                                                linkTo(methodOn(UserController.class).updatePassword(null, c.getId(), null)).withRel("updatepassword"),
						linkTo(methodOn(UserController.class).all()).withRel("user")))
				.collect(Collectors.toList());

		return CollectionModel.of(companies, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=roles_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
         
        List<User> list = this.provider.findAll();
 
        CSVWriter csvWriter = new CSVWriter(response.getWriter(), CSVPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Username", "Full Name"};
        String[] nameMapping = {"username", "fullName"};

        csvWriter.writeHeader(csvHeader);

        for (User i : list) {
            try {
                csvWriter.write(i, nameMapping);
            } catch (NoSuchFieldException ex) {
                log.error("Unable to find the Specified field in the Object.", ex);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Export failed!");
            }
        }
        return ResponseEntity.ok().build();
    }
    
}
