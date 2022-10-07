package com.tspdevelopment.kidsscore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.repository.GroupRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.GroupProviderImpl;
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/group")
public class GroupController extends BaseController<Group>{

    public GroupController(GroupRepository repository) {
        this.provider = new GroupProviderImpl(repository);
    }
    
    
    
    @GetMapping("/export")
    @RolesAllowed({Role.WRITE_ROLE, Role.ADMIN_ROLE })
    public void exportToCSV(HttpServletResponse response) throws IOException {
        String[] csvHeader = {"Name"};
        String[] nameMapping = {"name"};
        this.exportToCSV(response, csvHeader, nameMapping);
    }

}
