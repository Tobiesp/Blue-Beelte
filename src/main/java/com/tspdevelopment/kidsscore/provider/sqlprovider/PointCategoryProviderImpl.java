/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.kidsscore.provider.sqlprovider;

import com.tspdevelopment.kidsscore.data.model.PointCategory;
import com.tspdevelopment.kidsscore.data.repository.PointCategoryRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.PointCategoryProvider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Example;

/**
 *
 * @author tobiesp
 */
public class PointCategoryProviderImpl implements PointCategoryProvider {

    private final PointCategoryRepository repository;
    
    public PointCategoryProviderImpl(PointCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PointCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PointCategory> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public PointCategory update(PointCategory replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Updated item can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setCategory(replaceItem.getCategory());
                    item.setModifiedAt(LocalDateTime.now());
                    return repository.save(item);
                }) //
                .orElseGet(() -> {
                    return repository.save(replaceItem);
                });
    }

    @Override
    public PointCategory create(PointCategory newItem) {
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
    public List<PointCategory> search(PointCategory item) {
        Example<PointCategory> example = Example.of(item);
        return this.repository.findAll(example);
    }

    @Override
    public Optional<PointCategory> findByCategory(String category) {
        return this.repository.findByCategory(category);
    }
    
}
