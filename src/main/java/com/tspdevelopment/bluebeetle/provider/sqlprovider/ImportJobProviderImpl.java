package com.tspdevelopment.bluebeetle.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.bluebeetle.data.model.ImportJob;
import com.tspdevelopment.bluebeetle.data.repository.ImportJobRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.ImportJobProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ImportJobProviderImpl implements ImportJobProvider{

    private final ImportJobRepository repository;
    
    public ImportJobProviderImpl(ImportJobRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public List<ImportJob> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ImportJob> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public ImportJob create(ImportJob newItem) {
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
    public ImportJob update(ImportJob replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Updated ImportJob can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setFileName(replaceItem.getFileName());
                    item.setContent(replaceItem.getContent());
                    item.setModifiedAt(LocalDateTime.now());
                    return repository.save(item);
                }) //
                .orElseGet(() -> {
                    return repository.save(replaceItem);
                });
    }

    @Override
    public List<ImportJob> search(ImportJob item) {
        Example<ImportJob> example = Example.of(item);
        return this.repository.findAll(example);
    }

    @Override
    public Optional<ImportJob> findByFileName(String name) {
        return this.repository.findByFileName(name);
    }

    @Override
    public List<ImportJob> findByFileNameLike(String name) {
        return this.repository.findByFileNameLike(name);
    }

    @Override
    public Page<ImportJob> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<ImportJob> search(ImportJob item, Pageable pageable) {
        Example<ImportJob> example = Example.of(item);
        return this.repository.findAll(example, pageable);
    }
    
}
