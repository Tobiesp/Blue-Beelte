/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.PointsSpent;
import com.tspdevelopment.bluebeetle.data.model.RunningTotals;
import com.tspdevelopment.bluebeetle.data.repository.PointsSpentRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointsSpentProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.PointsSpentProviderImpl;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tobiesp
 */
public class PointsSpentService extends BaseService<PointsSpent, PointsSpentProvider> {
    
    private final RunningTotalsRepository rtRepository;
    
    public PointsSpentService(PointsSpentRepository repository, RunningTotalsRepository rtRepository) {
        this.provider = new PointsSpentProviderImpl(repository);
        this.rtRepository = rtRepository;
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
    
}
