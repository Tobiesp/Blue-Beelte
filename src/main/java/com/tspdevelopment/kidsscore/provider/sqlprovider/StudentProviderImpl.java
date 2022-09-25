package com.tspdevelopment.kidsscore.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.kidsscore.data.model.Student;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.StudentProvider;

public class StudentProviderImpl implements StudentProvider{

    private final StudentRepository repository;
    
    public StudentProviderImpl(StudentRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public List<Student> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Student> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public Student create(Student newItem) {
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
    public Student update(Student replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Updated Student can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setGrade(replaceItem.getGrade());
                    item.setGraduated(replaceItem.getGraduated());
                    item.setGroup(replaceItem.getGroup());
                    item.setName(replaceItem.getName());
                    item.setModifiedAt(LocalDateTime.now());
                    return repository.save(item);
                }) //
                .orElseGet(() -> {
                    return repository.save(replaceItem);
                });
    }

    @Override
    public List<Student> search(Student item) {
        Example<Student> example = Example.of(item);
        return this.repository.findAll(example);
    }

    public List<Student> findByGraduated(Date graduated){
        return this.repository.findByGraduated(graduated);
    }

    public List<Student> searchGraduated(Date start, Date end){
        return this.repository.searchGraduated(start, end);
    }
    
}
