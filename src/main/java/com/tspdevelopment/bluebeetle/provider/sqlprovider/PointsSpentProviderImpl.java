package com.tspdevelopment.bluebeetle.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.bluebeetle.data.model.PointsSpent;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.PointsSpentRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointsSpentProvider;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PointsSpentProviderImpl implements PointsSpentProvider {

    private final PointsSpentRepository repository;
    
    public PointsSpentProviderImpl(PointsSpentRepository repository) {
        this.repository = repository;
    }

    @Override
    public PointsSpent create(PointsSpent newItem) {
        newItem.setCreatedAt(LocalDateTime.now());
        PointsSpent sp = this.repository.save(newItem);
        return sp;
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
            throw new IllegalArgumentException("Item not found!");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setEventDate(replaceItem.getEventDate());
                    item.setTotal(replaceItem.getTotal());
                    item.setStudent(replaceItem.getStudent());
                    item.setModifiedAt(LocalDateTime.now());
                    PointsSpent sp = this.repository.save(item);
                    return sp;
                }) //
                .orElseGet(() -> {
                    PointsSpent sp = this.repository.save(replaceItem);
                    return sp;
                });
    }

    @Override
    public List<PointsSpent> search(PointsSpent item) {
        Example<PointsSpent> example = Example.of(item);
        return this.repository.findAll(example);
    }

    @Override
    public List<PointsSpent> findByEventDate(LocalDate eventDate){
        return this.repository.findByEventDate(eventDate);
    }
    
    @Override
    public List<PointsSpent> searchEventDate(LocalDate start, LocalDate end){
        return this.repository.searchEventDate(start, end);
    }

    @Override
    public List<PointsSpent> findByStudent(Student student) {
        return this.repository.findByStudent(student);
    }

    @Override
    public List<PointsSpent> searchStudentEventDate(Student student, LocalDate start, LocalDate end) {
        return this.repository.searchStudentEventDate(student, start, end);
    }

    @Override
    public Page<PointsSpent> findByStudent(Student student, Pageable pageable) {
        return this.repository.findByStudent(student, pageable);
    }

    @Override
    public Page<PointsSpent> findByEventDate(LocalDate eventDate, Pageable pageable) {
        return this.repository.findByEventDate(eventDate, pageable);
    }

    @Override
    public Page<PointsSpent> searchEventDate(LocalDate start, LocalDate end, Pageable pageable) {
        return this.repository.searchEventDate(start, end, pageable);
    }

    @Override
    public Page<PointsSpent> searchStudentEventDate(Student student, LocalDate start, LocalDate end, Pageable pageable) {
        return this.repository.searchStudentEventDate(student, start, end, pageable);
    }

    @Override
    public Page<PointsSpent> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<PointsSpent> search(PointsSpent item, Pageable pageable) {
        Example<PointsSpent> example = Example.of(item);
        return this.repository.findAll(example, pageable);
    }
    
}
