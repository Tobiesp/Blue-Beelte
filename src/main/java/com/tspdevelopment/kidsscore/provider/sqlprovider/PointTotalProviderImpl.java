package com.tspdevelopment.kidsscore.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.kidsscore.data.model.PointTotal;
import com.tspdevelopment.kidsscore.data.repository.PointTotalRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.PointTotalProvider;

public class PointTotalProviderImpl implements PointTotalProvider{

    private final PointTotalRepository repository;
    
    public PointTotalProviderImpl(PointTotalRepository repository) {
        this.repository = repository;
    }

    @Override
    public PointTotal create(PointTotal newItem) {
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
    public List<PointTotal> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PointTotal> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public PointTotal update(PointTotal replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Updated Student can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setGroup(replaceItem.getGroup());
                    item.setName(replaceItem.getName());
                    item.setPointTotal(replaceItem.getPointTotal());
                    item.setModifiedAt(LocalDateTime.now());
                    return repository.save(item);
                }) //
                .orElseGet(() -> {
                    return repository.save(replaceItem);
                });
    }

    @Override
    public List<PointTotal> search(PointTotal item) {
        Example<PointTotal> example = Example.of(item);
        return this.repository.findAll(example);
    }
    
}
