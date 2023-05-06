/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.PointType;
import com.tspdevelopment.bluebeetle.data.model.PointsEarned;
import com.tspdevelopment.bluebeetle.data.model.RunningTotals;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.PointTypeRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointsEarnedRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import com.tspdevelopment.bluebeetle.helpers.TimeHelper;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointTypeProvider;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointsEarnedProvider;
import com.tspdevelopment.bluebeetle.provider.interfaces.RunningTotalsProvider;
import com.tspdevelopment.bluebeetle.provider.interfaces.StudentProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.PointTypeProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.PointsEarnedProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.RunningTotalsProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.StudentProviderImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author tobiesp
 */
public class PointsEarnedService extends BaseService<PointsEarned, PointsEarnedProvider> {
    private final PointTypeProvider ptProvider;
    private final RunningTotalsProvider rtProvider;
    private final StudentProvider stdProvider;

    public PointsEarnedService(PointsEarnedRepository repository, PointTypeRepository ptRepository, RunningTotalsRepository rtRepository, StudentRepository stdRepository) {
        this.provider = new PointsEarnedProviderImpl(repository);
        this.ptProvider = new PointTypeProviderImpl(ptRepository);
        this.rtProvider = new RunningTotalsProviderImpl(rtRepository);
        this.stdProvider = new StudentProviderImpl(stdRepository);
    }
    
    private int getPointsForCategory(List<PointType> ptList, String label) {
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
        List<PointType> ptList = this.ptProvider.findByGroup(item.getStudent().getGroup());
        item.setTotal(getPointsForCategory(ptList, item.getPointCategory().getCategory()));
    }
    
    private void updateRunningTotal(PointsEarned newItem) {
        Optional<RunningTotals> runningTotal = rtProvider.findByStudent(newItem.getStudent());
        if(runningTotal.isPresent()) {
            RunningTotals rt = runningTotal.get();
            rt.setTotal(rt.getTotal()+newItem.getTotal());
            rtProvider.update(rt, rt.getId());
        } else {
            RunningTotals rt = new RunningTotals();
            rt.setCreatedAt(LocalDateTime.now());
            rt.setStudent(newItem.getStudent());
            rt.setTotal(newItem.getTotal());
            rtProvider.create(rt);
        }
    }
    
    public Student getStudent(UUID id) {
        Optional<Student> os = this.stdProvider.findById(id);
        return os.orElse(null);
    }
    
    @Override
    public PointsEarned getNewItem(PointsEarned newItem){
        updatePointValue(newItem);
        PointsEarned ep = provider.create(newItem);
        updateRunningTotal(ep);
        return ep;
    }
    
    public PointsEarned getNewItemNoTotalUpdate(PointsEarned newItem){
        PointsEarned ep = provider.create(newItem);
        updateRunningTotal(ep);
        return ep;
    }
    
    @Override
    public PointsEarned replaceItem(PointsEarned replaceItem, UUID id){
        PointsEarned ope = this.getItem(id);
        PointsEarned pe;
        if(ope != null){
            pe = provider.update(replaceItem, id);
            updateRunningTotal(pe);
        } else {
            updatePointValue(replaceItem);
            pe = provider.update(replaceItem, id);
            updateRunningTotal(pe);
        }
        return pe;
    }

    public List<PointsEarned> findByEventDate(LocalDate eventDate) {
        return this.provider.findByEventDate(eventDate);
    }

    public List<PointsEarned> searchEventDate(LocalDate start, LocalDate end) {
        return this.provider.searchEventDate(start, end);
    }

    public List<PointsEarned> findByStudent(Student student) {
        return this.provider.findByStudent(student);
    }

    public List<PointsEarned> searchStudentEventDate(Student student, LocalDate start, LocalDate end) {
        return this.provider.searchStudentEventDate(student, start, end);
    }

    public List<PointsEarned> findByStudentAndEventDate(Student student, LocalDate eventDate) {
        return this.provider.findByStudentAndEventDate(student, eventDate);
    }

    public LocalDate getLastEventDate() {
        return this.provider.getLastEventDate();
    }

    public Page<PointsEarned> findByEventDate(LocalDate eventDate, Pageable pageable) {
        return this.provider.findByEventDate(eventDate, pageable);
    }

    public Page<PointsEarned> searchEventDate(LocalDate start, LocalDate end, Pageable pageable) {
        return this.provider.searchEventDate(start, end, pageable);
    }

    public Page<PointsEarned> findByStudent(Student student, Pageable pageable) {
        return this.provider.findByStudent(student, pageable);
    }

    public Page<PointsEarned> searchStudentEventDate(Student student, LocalDate start, LocalDate end, Pageable pageable) {
        return this.provider.searchStudentEventDate(student, start, end, pageable);
    }

    public Page<PointsEarned> findByStudentAndEventDate(Student student, LocalDate eventDate, Pageable pageable) {
        return this.provider.findByStudentAndEventDate(student, eventDate, pageable);
    }

    public List<PointsEarned> getPossiblePoints(Student std) {
        List<PointType> ptList = ptProvider.findByGroup(std.getGroup());
        List<PointsEarned> list = new ArrayList<>();
        //TODO: Filter out points that are outside of cycle time.
        for(PointType pt: ptList) {
            PointsEarned pe = new PointsEarned();
            pe.setStudent(std);
            pe.setCreatedAt(LocalDateTime.now());
            pe.setEventDate(TimeHelper.getInstance().getPreviousEventDate());
            pe.setPointCategory(pt.getPointCategory());
            pe.setTotal(pt.getTotalPoints());
            list.add(pe);
        }
        return list;
    }
    
}
