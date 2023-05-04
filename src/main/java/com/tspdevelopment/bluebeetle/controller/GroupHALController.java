package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.csv.importmodels.GroupV1;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.repository.GroupRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.GroupProvider;
import com.tspdevelopment.bluebeetle.response.ImportJobResponse;
import com.tspdevelopment.bluebeetle.services.controllerservice.GroupService;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/hal/group")
public class GroupHALController extends BaseHALController<Group, GroupProvider, GroupService>{

    public GroupHALController(GroupRepository repository) {
        this.service = new GroupService(repository);
        this.AddLinkForSingle(linkTo(methodOn(this.getClass()).findByName(placeHolder)).withRel("findByName"));
        this.AddLinkForList(linkTo(methodOn(this.getClass()).findByName(placeHolder)).withRel("findByName"));
    }
    
    @GetMapping("/findByName")
    @RolesAllowed({Role.READ_ROLE, Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public EntityModel<Group> findByName(@RequestParam Optional<String> name) {
        if(!name.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must be supplied.s");
        }
        return getModelForSingle(this.service.findByName(name.get()));
    }
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public ResponseEntity<?> exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Name"};
        String[] nameMapping = {"name"};
        return this.baseExportToCSV(response, csvHeader, nameMapping);
    }
    
    
    @PostMapping("/import")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> CSVImport(@RequestParam("file") MultipartFile file) throws IOException {
        return this.CSVImportV1(file);
    }
    
    
    @PostMapping("/import/v1")
    @RolesAllowed({Role.ADMIN_ROLE })
    public ResponseEntity<?> CSVImportV1(@RequestParam("file") MultipartFile file) throws IOException {
        ImportJobResponse response = this.baseImportCSV(file, GroupV1.class);
        return ResponseEntity.ok().body(response);
    }

}
