/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.repository.GroupRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.GroupProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.GroupProviderImpl;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author tobiesp
 */
@Service
public class GroupService extends BaseService<Group, GroupProvider> {

    public GroupService(GroupRepository repository) {
        this.provider = new GroupProviderImpl(repository);
    }
    
    public Group findByName(String name) {
        Optional<Group> og = this.provider.findByName(name);
        return og.isPresent() ? og.get() : null;
    }

    public List<Group> findByNameLike(String name) {
        return this.provider.findByNameLike(name);
    }

    public Page<Group> findByNameLike(String name, Pageable pageable) {
        return this.provider.findByNameLike(name, pageable);
    }
    
}
