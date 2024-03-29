package com.tspdevelopment.bluebeetle.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.UserProvider;

public class UserProviderImpl implements UserProvider {

    private final UserRepository repository;
    
    public UserProviderImpl(UserRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public User update(User replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        Optional<User> oUser = this.repository.findById(id);
        if(!oUser.isPresent()) {
            return repository.save(replaceItem);
        } else {
            User user = oUser.get();
            user.setFirstName(replaceItem.getFirstName());
            user.setLastName(replaceItem.getLastName());
            user.setRoles(replaceItem.getRoles());
            user.setEmail(replaceItem.getEmail());
            user.setModifiedAt(LocalDateTime.now());
            return repository.save(user);
        }
    }

    @Override
    public void delete(UUID id) {
        this.repository.deleteById(id);
    }

    @Override
    public List<User> search(User item) {
        Example<User> example = Example.of(item);
        return this.repository.findAll(example);
    }

    @Override
    public User create(User item) {
        if((item != null) && (item.getCreatedAt() == null)) {
            item.setCreatedAt(LocalDateTime.now());
            item.setModifiedAt(LocalDateTime.now());
        }
        return this.repository.save(item);
    }

    @Override
    public User updatePassword(UUID id, String password) {
        if (id == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        Optional<User> oUser = this.findById(id);
        if(!oUser.isPresent()) {
            throw new IllegalArgumentException("Item not found.");
        } else {
            User user = oUser.get();
            user.setPassword(password);
            user.setModifiedAt(LocalDateTime.now());
            return repository.save(user);
        }
    }

    @Override
    public void updateJwtTokenId(UUID userId, UUID tokenId) {
        if (userId == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        Optional<User> oUser = this.findById(userId);
        if(!oUser.isPresent()) {
            throw new IllegalArgumentException("Item not found.");
        } else {
            User user = oUser.get();
            user.setTokenId(tokenId);
            user.setModifiedAt(LocalDateTime.now());
            repository.save(user);
        }
    }

    @Override
    public void increaseLoginAttempt(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        Optional<User> user = this.repository.findByUsername(username);
        if(user.isPresent()) {
            user.get().setFailedAttempt(user.get().getFailedAttempt()+1);
            this.repository.save(user.get());
        }
    }

}
