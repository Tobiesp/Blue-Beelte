/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.data.model.RunningTotals;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.RunningTotalsProviderImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/points/total/running")
public class RunningTotalsController extends BaseController<RunningTotals>{
    
    public RunningTotalsController(RunningTotalsRepository repository) {
        this.provider = new RunningTotalsProviderImpl(repository);
    }
    
}
