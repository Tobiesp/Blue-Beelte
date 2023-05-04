package com.tspdevelopment.bluebeetle.provider.sqlprovider;

import com.tspdevelopment.bluebeetle.data.model.Group;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.StudentProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
            throw new IllegalArgumentException("Item not found.");
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

    @Override
    public List<Student> findByGraduated(Date graduated){
        return this.repository.findByGraduated(graduated);
    }

    @Override
    public List<Student> searchGraduated(Date start, Date end){
        return this.repository.searchGraduated(start, end);
    }

    @Override
    public Optional<Student> findByName(String name) {
        return this.repository.findByName(name);
    }

    @Override
    public List<Student> findByNameLike(String name) {
        return this.repository.findByNameLike(name);
    }

    @Override
    public List<Student> findByGroup(Group group) {
        return this.repository.findByGroup(group);
    }

    @Override
    public List<Student> findByGrade(int grade) {
        return this.repository.findByGrade(grade);
    }

    @Override
    public Page<Student> findByNameLike(String name, Pageable pageable) {
        return this.repository.findByNameLike(name, pageable);
    }

    @Override
    public Page<Student> findByGraduated(Date graduated, Pageable pageable) {
        return this.repository.findByGraduated(graduated, pageable);
    }

    @Override
    public Page<Student> searchGraduated(Date start, Date end, Pageable pageable) {
        return this.repository.searchGraduated(start, end, pageable);
    }

    @Override
    public Page<Student> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Student> search(Student item, Pageable pageable) {
        Example<Student> example = Example.of(item);
        return this.repository.findAll(example, pageable);
    }

    @Override
    public Page<Student> findByGroup(Group group, Pageable pageable) {
        return this.repository.findByGroup(group, pageable);
    }

    @Override
    public Page<Student> findByGrade(int grade, Pageable pageable) {
        return this.repository.findByGrade(grade, pageable);
    }
    
}
