package com.tspdevelopment.bluebeetle.provider.sqlprovider;

import com.tspdevelopment.bluebeetle.provider.interfaces.JWTSecretProvider;
import com.tspdevelopment.bluebeetle.data.repository.*;
import com.tspdevelopment.bluebeetle.data.model.*;
import java.util.*;

import java.time.LocalDateTime;

import org.springframework.data.domain.Example;

public class JWTSecretProviderImpl implements JWTSecretProvider{

    private final JWTSecretRepository repository;
    
    public JWTSecretProviderImpl(JWTSecretRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public List<JWTSecret> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<JWTSecret> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public JWTSecret create(JWTSecret newItem) {
        if((newItem != null) && (newItem.getCreatedAt() == null)) {
            newItem.setCreatedAt(LocalDateTime.now());
            newItem.setModifiedAt(LocalDateTime.now());
        }
        return this.repository.save(newItem);
    }

    @Override
    public void delete(UUID id) {
        this.repository.deleteById(id);
        
    }

    @Override
    public JWTSecret update(JWTSecret replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Updated Student can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setSecret(replaceItem.getSecret());
                    item.setSecretId(replaceItem.getSecretId());
                    item.setModifiedAt(LocalDateTime.now());
                    return repository.save(item);
                }) //
                .orElseGet(() -> {
                    return repository.save(replaceItem);
                });
    }

    @Override
    public void cleanUpSecret(LocalDateTime time) {
        this.repository.deleteByCreatedAtBefore(time);
    }

    @Override
    public List<JWTSecret> search(JWTSecret item) {
        Example<JWTSecret> example = Example.of(item);
        return this.repository.findAll(example);
    }

    @Override
    public Optional<JWTSecret> findBySecretId(String secretId) {
        return this.repository.findBySecretId(secretId);
    }
    
}
