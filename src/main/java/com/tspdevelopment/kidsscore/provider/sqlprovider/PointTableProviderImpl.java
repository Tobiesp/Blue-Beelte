package com.tspdevelopment.kidsscore.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.PointCategory;
import com.tspdevelopment.kidsscore.data.model.PointTable;
import com.tspdevelopment.kidsscore.data.repository.PointTableRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.PointTableProvider;

public class PointTableProviderImpl implements PointTableProvider{

    private final PointTableRepository repository;
    
    public PointTableProviderImpl(PointTableRepository repository) {
        this.repository = repository;
    }

    @Override
    public PointTable create(PointTable newItem) {
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
    public List<PointTable> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PointTable> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public PointTable update(PointTable replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Updated item can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setGroup(replaceItem.getGroup());
                    item.setPointCategory(replaceItem.getPointCategory());
                    item.setEnabled(replaceItem.isEnabled());
                    item.setTotalPoints(replaceItem.getTotalPoints());
                    item.setModifiedAt(LocalDateTime.now());
                    return repository.save(item);
                }) //
                .orElseGet(() -> {
                    return repository.save(replaceItem);
                });
    }

    @Override
    public List<PointTable> search(PointTable item) {
        Example<PointTable> example = Example.of(item);
        return this.repository.findAll(example);
    }

    @Override
    public List<PointTable> findByGroup(Group group){
        return this.repository.findByGroup(group);
    }

    @Override
    public List<PointTable> findByPointCategory(PointCategory pointCategory) {
        return this.repository.findByPointCategory(pointCategory);
    }
    
}
