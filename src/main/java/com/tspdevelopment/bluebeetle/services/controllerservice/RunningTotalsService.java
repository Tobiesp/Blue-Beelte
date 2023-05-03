/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.RunningTotals;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.RunningTotalsProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.RunningTotalsProviderImpl;

/**
 *
 * @author tobiesp
 */
public class RunningTotalsService extends BaseService<RunningTotals, RunningTotalsProvider> {

    public RunningTotalsService(RunningTotalsRepository repository) {
        this.provider = new RunningTotalsProviderImpl(repository);
    }
    
}
