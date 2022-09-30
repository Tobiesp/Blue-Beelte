package com.tspdevelopment.kidsscore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.repository.GroupRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.GroupProviderImpl;

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

}
