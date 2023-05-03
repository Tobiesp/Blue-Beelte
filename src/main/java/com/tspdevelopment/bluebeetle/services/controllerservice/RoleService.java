/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.data.repository.RoleRepository;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.RoleProvider;
import com.tspdevelopment.bluebeetle.provider.interfaces.UserProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.RoleProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.UserProviderImpl;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tobiesp
 */
public class RoleService extends BaseService<Role, RoleProvider> {
    
    private UserProvider userProvider;
    
    public RoleService(RoleRepository repository, UserRepository userRepository) {
        this.provider = new RoleProviderImpl(repository);
        this.userProvider = new UserProviderImpl(userRepository);
    }
    
    public User getUserById(UUID id) {
        Optional<User> ou = this.userProvider.findById(id);
        if(ou.isPresent()) {
            return ou.get();
        }
        return null;
    }
    
}
