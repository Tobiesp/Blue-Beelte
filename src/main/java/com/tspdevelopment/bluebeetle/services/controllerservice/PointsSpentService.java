package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.PointsSpent;
import com.tspdevelopment.bluebeetle.data.model.RunningTotals;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.PointsSpentRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointsSpentProvider;
import com.tspdevelopment.bluebeetle.provider.interfaces.StudentProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.PointsSpentProviderImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author tobiesp
 */
public class PointsSpentService extends BaseService<PointsSpent, PointsSpentProvider> {
    
    private final RunningTotalsRepository rtRepository;
    private final StudentRepository stdRepository;
    
    public PointsSpentService(PointsSpentRepository repository, RunningTotalsRepository rtRepository, StudentRepository stdRepository) {
        this.provider = new PointsSpentProviderImpl(repository);
        this.rtRepository = rtRepository;
        this.stdRepository = stdRepository;
    }
    
    private void updateRunningTotal(PointsSpent newItem) {
        Optional<RunningTotals> runningTotal = rtRepository.findByStudent(newItem.getStudent());
        if(runningTotal.isPresent()) {
            RunningTotals rt = runningTotal.get();
            rt.setTotal(rt.getTotal() - newItem.getTotal());
            rtRepository.save(rt);
        } else {
            RunningTotals rt = new RunningTotals();
            rt.setCreatedAt(LocalDateTime.now());
            rt.setStudent(newItem.getStudent());
            rt.setTotal(0 - newItem.getTotal());
            rtRepository.save(rt);
        }
    }
    
    public Optional<Student> getStudent(UUID id) {
        return this.stdRepository.findById(id);
    }
    
    @Override
    public PointsSpent getNewItem(PointsSpent newItem){
        PointsSpent ps = provider.create(newItem);
        this.updateRunningTotal(ps);
        return ps;
    }
    
    @Override
    public PointsSpent replaceItem(PointsSpent replaceItem, UUID id){
        PointsSpent ps = provider.update(replaceItem, id);
        this.updateRunningTotal(ps);
        return ps;
    }

    public List<PointsSpent> findByEventDate(LocalDate eventDate){
        return this.provider.findByEventDate(eventDate);
    }
    
    public List<PointsSpent> searchEventDate(LocalDate start, LocalDate end){
        return this.provider.searchEventDate(start, end);
    }

    public List<PointsSpent> findByStudent(Student student) {
        return this.provider.findByStudent(student);
    }

    public List<PointsSpent> searchStudentEventDate(Student student, LocalDate start, LocalDate end) {
        return this.provider.searchStudentEventDate(student, start, end);
    }

    public Page<PointsSpent> findByStudent(Student student, Pageable pageable) {
        return this.provider.findByStudent(student, pageable);
    }

    public Page<PointsSpent> findByEventDate(LocalDate eventDate, Pageable pageable) {
        return this.provider.findByEventDate(eventDate, pageable);
    }

    public Page<PointsSpent> searchEventDate(LocalDate start, LocalDate end, Pageable pageable) {
        return this.provider.searchEventDate(start, end, pageable);
    }

    public Page<PointsSpent> searchStudentEventDate(Student student, LocalDate start, LocalDate end, Pageable pageable) {
        return this.provider.searchStudentEventDate(student, start, end, pageable);
    }
    
}
