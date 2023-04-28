package com.tspdevelopment.bluebeetle.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.bluebeetle.data.model.PointsSpent;
import com.tspdevelopment.bluebeetle.data.model.RunningTotals;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.PointsSpentRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointsSpentProvider;
import java.time.LocalDate;

public class PointsSpentProviderImpl implements PointsSpentProvider {

    private final PointsSpentRepository repository;
    private final RunningTotalsRepository rtRepository;
    
    public PointsSpentProviderImpl(PointsSpentRepository repository, 
                                    RunningTotalsRepository rtRepository) {
        this.repository = repository;
        this.rtRepository = rtRepository;
    }

    @Override
    public PointsSpent create(PointsSpent newItem) {
        if((newItem != null) && (newItem.getCreatedAt() == null)) {
            newItem.setCreatedAt(LocalDateTime.now());
            newItem.setModifiedAt(LocalDateTime.now());
        }
        PointsSpent sp = this.repository.save(newItem);
        updateRunningTotal(sp);
        return sp;
    }
    
    private void updateRunningTotal(PointsSpent newItem) {
        Optional<RunningTotals> runningTotal = rtRepository.findByStudent(newItem.getStudent());
        if(runningTotal.isPresent()) {
            RunningTotals rt = runningTotal.get();
            rt.setTotal(rt.getTotal() - newItem.getPoints());
            rtRepository.save(rt);
        } else {
            RunningTotals rt = new RunningTotals();
            rt.setCreatedAt(LocalDateTime.now());
            rt.setStudent(newItem.getStudent());
            rt.setTotal(0 - newItem.getPoints());
            rtRepository.save(rt);
        }
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
                    item.setPoints(replaceItem.getPoints());
                    item.setStudent(replaceItem.getStudent());
                    item.setModifiedAt(LocalDateTime.now());
                    PointsSpent sp = this.repository.save(item);
                    updateRunningTotal(sp);
                    return sp;
                }) //
                .orElseGet(() -> {
                    PointsSpent sp = this.repository.save(replaceItem);
                    updateRunningTotal(sp);
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
    
}
