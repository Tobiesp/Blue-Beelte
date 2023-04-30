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

import org.springframework.hateoas.CollectionModel;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/role")
public class RoleController {
    
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(RoleController.class);
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
    
    @GetMapping("/export")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=roles_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
         
        List<Role> listRole = this.provider.findAll();
 
        CSVWriter csvWriter = new CSVWriter(response.getWriter(), CSVPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Name"};
        String[] nameMapping = {"authority"};

        csvWriter.writeHeader(csvHeader);

        for (Role role : listRole) {
            try {
                csvWriter.write(role, nameMapping);
            } catch (NoSuchFieldException ex) {
                logger.error("Unable to find the Specified field in the Object.", ex);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Export failed!");
            }
        }
        return ResponseEntity.ok().build();
    }
    
}
