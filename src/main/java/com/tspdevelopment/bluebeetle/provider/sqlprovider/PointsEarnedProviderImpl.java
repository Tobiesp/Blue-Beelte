package com.tspdevelopment.bluebeetle.provider.sqlprovider;

import com.tspdevelopment.bluebeetle.data.model.PointType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.bluebeetle.data.model.PointsEarned;
import com.tspdevelopment.bluebeetle.data.model.RunningTotals;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.PointTypeRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointsEarnedRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointsEarnedProvider;
import java.time.LocalDate;

public class PointsEarnedProviderImpl implements PointsEarnedProvider{

    private final PointsEarnedRepository repository;
    private final PointTypeRepository ptRepository;
    private final RunningTotalsRepository rtRepository;
    
    public PointsEarnedProviderImpl(PointsEarnedRepository repository, 
                                    PointTypeRepository ptRepository, 
                                    RunningTotalsRepository rtRepository) {
        this.repository = repository;
        this.ptRepository = ptRepository;
        this.rtRepository = rtRepository;
    }
    
    @Override
    public List<PointsEarned> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PointsEarned> findById(UUID id) {
        return this.repository.findById(id);
    }
    
    private int findPointTable(List<PointType> ptList, String label) {
        if(label == null) {
            return 0;
        }
        for(PointType pt: ptList ) {
            if(pt.getPointCategory().getCategory().equalsIgnoreCase(label)) {
                return pt.getTotalPoints();
            }
        }
        return 0;
    }
    
    private void updatePointValue(PointsEarned item) {
        if(item.getStudent() == null) {
            return;
        }
        List<PointType> ptList = this.ptRepository.findByGroup(item.getStudent().getGroup());
        item.setTotal(findPointTable(ptList, item.getPointCategory().getCategory()));
    }

    @Override
    public PointsEarned create(PointsEarned newItem) {
        if((newItem != null) && (newItem.getCreatedAt() == null)) {
            newItem.setCreatedAt(LocalDateTime.now());
            newItem.setModifiedAt(LocalDateTime.now());
        }
        updatePointValue(newItem);
        PointsEarned pe = this.repository.save(newItem);
        updateRunningTotal(pe);
        return pe;
    }
    
    private void updateRunningTotal(PointsEarned newItem) {
        Optional<RunningTotals> runningTotal = rtRepository.findByStudent(newItem.getStudent());
        if(runningTotal.isPresent()) {
            RunningTotals rt = runningTotal.get();
            rt.setTotal(rt.getTotal()+newItem.getTotal());
            rtRepository.save(rt);
        } else {
            RunningTotals rt = new RunningTotals();
            rt.setCreatedAt(LocalDateTime.now());
            rt.setStudent(newItem.getStudent());
            rt.setTotal(newItem.getTotal());
            rtRepository.save(rt);
        }
    }
    

    @Override
    public PointsEarned createNoTotalUpdate(PointsEarned newItem) {
        if((newItem != null) && (newItem.getCreatedAt() == null)) {
            newItem.setCreatedAt(LocalDateTime.now());
            newItem.setModifiedAt(LocalDateTime.now());
        }
        PointsEarned pe = this.repository.save(newItem);
        updateRunningTotal(pe);
        return pe;
    }

    @Override
    public void delete(UUID id) {
        this.repository.deleteById(id);
        
    }

    @Override
    public PointsEarned update(PointsEarned replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Updated PointsEarned item can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setEventDate(replaceItem.getEventDate());
                    item.setStudent(replaceItem.getStudent());
                    item.setPointCategory(replaceItem.getPointCategory());
                    item.setModifiedAt(LocalDateTime.now());
                    updatePointValue(item);
                    PointsEarned i = repository.save(item);
                    updateRunningTotal(i);
                    return i;
                }) //
                .orElseGet(() -> {
                    PointsEarned i = repository.save(replaceItem);
                    updateRunningTotal(i);
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
    public LocalDate getLastEventDate() {
        List<LocalDate> dates = this.repository.getLastEventDate();
        return dates.isEmpty()?null:dates.get(0);
    }

    
    
}
