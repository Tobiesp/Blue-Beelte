/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.UserProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.UserProviderImpl;
import java.util.UUID;

/**
 *
 * @author tobiesp
 */
public class UserService extends BaseService<User, UserProvider> {

    public UserService(UserRepository repository) {
        this.provider = new UserProviderImpl(repository);
    }

    public User updatePassword(UUID id, String password) {
        return this.provider.updatePassword(id, password);
    }

    public void updateJwtTokenId(UUID userId, UUID tokenId) {
        this.provider.updateJwtTokenId(userId, tokenId);
    }

    public void increaseLoginAttempt(String username) {
        this.provider.increaseLoginAttempt(username);
    }
    
    public User findByUsername(String username) {
        return this.provider.findByUsername(username);
    }
    
    public User fingByEmail(String email) {
        return this.provider.findByEmail(email);
    }
    
}
