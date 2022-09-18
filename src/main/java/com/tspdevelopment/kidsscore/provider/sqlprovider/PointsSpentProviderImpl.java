package com.tspdevelopment.kidsscore.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.kidsscore.data.model.PointsSpent;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.PointsSpentProvider;

public class PointsSpentProviderImpl implements PointsSpentProvider {

    private final PointsSpentRepository repository;
    
    public PointsSpentProviderImpl(PointsSpentRepository repository) {
        this.repository = repository;
    }

    @Override
    public PointsSpent create(PointsSpent newItem) {
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
    public List<PointsSpent> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PointsSpent> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public PointsSpent update(PointsSpent replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Updated Student can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setEventDate(replaceItem.getEventDate());
                    item.setPointsSpent(replaceItem.getPointsSpent());
                    item.setStudent(replaceItem.getStudent());
                    item.setModifiedAt(LocalDateTime.now());
                    return repository.save(item);
                }) //
                .orElseGet(() -> {
                    return repository.save(replaceItem);
                });
    }

    @Override
    public List<PointsSpent> search(PointsSpent item) {
        Example<PointsSpent> example = Example.of(item);
        return this.repository.findAll(example);
    }
    
}
