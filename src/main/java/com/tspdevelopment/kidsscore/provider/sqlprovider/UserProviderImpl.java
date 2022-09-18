package com.tspdevelopment.kidsscore.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.kidsscore.data.model.User;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.UserProvider;

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
            throw new IllegalArgumentException("Updated User can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                                        item.setFullName(replaceItem.getFullName());
                                        item.setRoles(replaceItem.getRoles());
                                        item.setModifiedAt(LocalDateTime.now());
                    return repository.save(item);
                }) //
                .orElseGet(() -> {
                    return repository.save(replaceItem);
                });
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
    public User updatePassowrd(UUID id, String password) {
        if (id == null) {
            throw new IllegalArgumentException("User id can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                                        item.setPassword(password);
                    return repository.save(item);
                }).orElseThrow();
    }

    @Override
    public void updateJwtTokenId(UUID userId, UUID tokenId) {
        if (userId == null) {
            throw new IllegalArgumentException("User id can not be null.");
        }
        repository.findById(userId) //
                .map(item -> {
                                        item.setTokenId(tokenId);
                    return repository.save(item);
                }).orElseThrow();
    }

}
