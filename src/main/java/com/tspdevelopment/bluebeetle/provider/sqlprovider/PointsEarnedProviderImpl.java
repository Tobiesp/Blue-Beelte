package com.tspdevelopment.bluebeetle.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.bluebeetle.data.model.PointsEarned;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.PointsEarnedRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointsEarnedProvider;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PointsEarnedProviderImpl implements PointsEarnedProvider{

    private final PointsEarnedRepository repository;
    
    public PointsEarnedProviderImpl(PointsEarnedRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public List<PointsEarned> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PointsEarned> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public PointsEarned create(PointsEarned newItem) {
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
    public PointsEarned update(PointsEarned replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setEventDate(replaceItem.getEventDate());
                    item.setStudent(replaceItem.getStudent());
                    item.setPointCategory(replaceItem.getPointCategory());
                    item.setModifiedAt(LocalDateTime.now());
                    PointsEarned i = repository.save(item);
                    return i;
                }) //
                .orElseGet(() -> {
                    PointsEarned i = repository.save(replaceItem);
                    return i;
                });
    }

    @Override
    public List<PointsEarned> search(PointsEarned item) {
        Example<PointsEarned> example = Example.of(item);
        return this.repository.findAll(example);
    }

    @Override
    public List<PointsEarned> findByEventDate(LocalDate eventDate) {
        return this.repository.findByEventDate(eventDate);
    }

    @Override
    public List<PointsEarned> searchEventDate(LocalDate start, LocalDate end) {
        return this.repository.searchEventDate(start, end);
    }

    @Override
    public List<PointsEarned> findByStudent(Student student) {
        return this.repository.findByStudent(student);
    }

    @Override
    public List<PointsEarned> searchStudentEventDate(Student student, LocalDate start, LocalDate end) {
        return this.repository.searchStudentEventDate(student, start, end);
    }
    
    @Override
    public List<PointsEarned> findByStudentAndEventDate(Student student, LocalDate eventDate) {
        return this.repository.findByStudentAndEventDate(student, eventDate);
    }

    @Override
    public LocalDate getLastEventDate() {
        List<LocalDate> dates = this.repository.getLastEventDate();
        return dates.isEmpty()?null:dates.get(0);
    }

    @Override
    public Page<PointsEarned> findByStudent(Student student, Pageable pageable) {
        return this.repository.findByStudent(student, pageable);
    }

    @Override
    public Page<PointsEarned> findByEventDate(LocalDate eventDate, Pageable pageable) {
        return this.repository.findByEventDate(eventDate, pageable);
    }

    @Override
    public Page<PointsEarned> searchEventDate(LocalDate start, LocalDate end, Pageable pageable) {
        return this.repository.searchEventDate(start, end, pageable);
    }

    @Override
    public Page<PointsEarned> searchStudentEventDate(Student student, LocalDate start, LocalDate end, Pageable pageable) {
        return this.repository.searchStudentEventDate(student, start, end, pageable);
    }

    @Override
    public Page<PointsEarned> findByStudentAndEventDate(Student student, LocalDate eventDate, Pageable pageable) {
        return this.repository.findByStudentAndEventDate(student, eventDate, pageable);
    }

    @Override
    public Page<PointsEarned> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<PointsEarned> search(PointsEarned item, Pageable pageable) {
        Example<PointsEarned> example = Example.of(item);
        return this.repository.findAll(example, pageable);
    }

    
    
}
