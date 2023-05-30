package com.tspdevelopment.bluebeetle.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import com.tspdevelopment.bluebeetle.data.model.PointType;
import com.tspdevelopment.bluebeetle.data.repository.PointTypeRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointTypeProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PointTypeProviderImpl implements PointTypeProvider{

    private final PointTypeRepository repository;
    
    public PointTypeProviderImpl(PointTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public PointType create(PointType newItem) {
        newItem.setCreatedAt(LocalDateTime.now());
        return this.repository.save(newItem);
    }

    @Override
    public void delete(UUID id) {
        this.repository.deleteById(id);
        
    }

    @Override
    public List<PointType> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PointType> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public PointType update(PointType replaceItem, UUID id) {
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
    public List<PointType> search(PointType item) {
        Example<PointType> example = Example.of(item);
        return this.repository.findAll(example);
    }

    @Override
    public List<PointType> findByGroup(Group group){
        return this.repository.findByGroup(group);
    }

    @Override
    public List<PointType> findByCategory(PointCategory pointCategory) {
        return this.repository.findByPointCategory(pointCategory);
    }

    @Override
    public List<PointType> findByCategoryAndGroup(PointCategory pointCategory, Group group) {
        return this.repository.findByPointCategoryAndGroup(pointCategory, group);
    }

    @Override
    public Page<PointType> findByGroup(Group group, Pageable pageable) {
        return this.repository.findByGroup(group, pageable);
    }

    @Override
    public Page<PointType> findByCategory(PointCategory pointCategory, Pageable pageable) {
        return this.repository.findByPointCategory(pointCategory, pageable);
    }

    @Override
    public Page<PointType> findByCategoryAndGroup(PointCategory pointCategory, Group group, Pageable pageable) {
        return this.repository.findByPointCategoryAndGroup(pointCategory, group, pageable);
    }

    @Override
    public Page<PointType> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<PointType> search(PointType item, Pageable pageable) {
        Example<PointType> example = Example.of(item);
        return this.repository.findAll(example, pageable);
    }
    
}
