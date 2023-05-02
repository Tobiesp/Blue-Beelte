package com.tspdevelopment.bluebeetle.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.bluebeetle.data.model.RunningTotals;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.RunningTotalsProvider;

public class RunningTotalsProviderImpl implements RunningTotalsProvider{

    private final RunningTotalsRepository repository;
    
    public RunningTotalsProviderImpl(RunningTotalsRepository repository) {
        this.repository = repository;
    }

    @Override
    public RunningTotals create(RunningTotals newItem) {
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
    public List<RunningTotals> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<RunningTotals> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public RunningTotals update(RunningTotals replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setStudent(replaceItem.getStudent());
                    item.setTotal(replaceItem.getTotal());
                    item.setModifiedAt(LocalDateTime.now());
                    return repository.save(item);
                }) //
                .orElseGet(() -> {
                    return repository.save(replaceItem);
                });
    }

    @Override
    public List<RunningTotals> search(RunningTotals item) {
        Example<RunningTotals> example = Example.of(item);
        return this.repository.findAll(example);
    }

    @Override
    public Optional<RunningTotals> findByStudent(Student student) {
        return this.repository.findByStudent(student);
    }
    
}
