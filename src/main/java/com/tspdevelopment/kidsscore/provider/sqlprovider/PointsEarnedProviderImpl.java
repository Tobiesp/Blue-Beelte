package com.tspdevelopment.kidsscore.provider.sqlprovider;

import com.tspdevelopment.kidsscore.data.model.PointTable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.kidsscore.data.model.PointsEarned;
import com.tspdevelopment.kidsscore.data.model.Student;
import com.tspdevelopment.kidsscore.data.repository.PointTableRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsEarnedRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.helpers.UpdateTotals;
import com.tspdevelopment.kidsscore.provider.interfaces.PointsEarnedProvider;
import java.time.LocalDate;

public class PointsEarnedProviderImpl implements PointsEarnedProvider{

    private final PointsEarnedRepository repository;
    private final PointTableRepository ptRepository;
    private final PointsSpentRepository psRepository;
    private final RunningTotalsRepository rtRepository;
    
    public PointsEarnedProviderImpl(PointsEarnedRepository repository, PointTableRepository ptRepository, PointsSpentRepository psRepository, RunningTotalsRepository rtRepository) {
        this.repository = repository;
        this.ptRepository = ptRepository;
        this.psRepository = psRepository;
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
    
    private int findPointTable(List<PointTable> ptList, String label) {
        if(label == null) {
            return 0;
        }
        for(PointTable pt: ptList ) {
            if(pt.getPointCategory().getCategory().equalsIgnoreCase(label)) {
                return pt.getTotalPoints();
            }
        }
        return 0;
    }
    
    private void updateTotals(PointsEarned item) {
        if(item.getStudent() == null) {
            return;
        }
        List<PointTable> ptList = this.ptRepository.findByGroup(item.getStudent().getGroup());
        item.setTotal(findPointTable(ptList, item.getPointCategory().getCategory()));
    }

    @Override
    public PointsEarned create(PointsEarned newItem) {
        if((newItem != null) && (newItem.getCreatedAt() == null)) {
            newItem.setCreatedAt(LocalDateTime.now());
            newItem.setModifiedAt(LocalDateTime.now());
        }
        updateTotals(newItem);
        PointsEarned pe = this.repository.save(newItem);
        UpdateTotals.getInstance().setPointsEarnedRepository(repository);
        UpdateTotals.getInstance().setPointsSpentRepository(psRepository);
        UpdateTotals.getInstance().setRunningTotalsRepository(rtRepository);
        UpdateTotals.getInstance().updateTotal(pe.getStudent());
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
                    updateTotals(item);
                    PointsEarned i = repository.save(item);
                    UpdateTotals.getInstance().setPointsEarnedRepository(repository);
                    UpdateTotals.getInstance().setPointsSpentRepository(psRepository);
                    UpdateTotals.getInstance().setRunningTotalsRepository(rtRepository);
                    UpdateTotals.getInstance().updateTotal(item.getStudent());
                    return i;
                }) //
                .orElseGet(() -> {
                    PointsEarned i = repository.save(replaceItem);
                    UpdateTotals.getInstance().setPointsEarnedRepository(repository);
                    UpdateTotals.getInstance().setPointsSpentRepository(psRepository);
                    UpdateTotals.getInstance().setRunningTotalsRepository(rtRepository);
                    UpdateTotals.getInstance().updateTotal(replaceItem.getStudent());
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

    
    
}
