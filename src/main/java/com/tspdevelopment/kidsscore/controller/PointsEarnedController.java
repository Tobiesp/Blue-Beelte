/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.kidsscore.controller;

import com.tspdevelopment.kidsscore.data.model.PointsEarned;
import com.tspdevelopment.kidsscore.data.repository.PointTableRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsEarnedRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.provider.sqlprovider.PointsEarnedProviderImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tobiesp
 */
@RestController
@RequestMapping("/api/points/earned")
public class PointsEarnedController extends BaseController<PointsEarned>{
    
    public PointsEarnedController(PointsEarnedRepository repository, PointTableRepository ptRepository, PointsSpentRepository psRepository, RunningTotalsRepository rtRepository) {
        this.provider = new PointsEarnedProviderImpl(repository, ptRepository, psRepository, rtRepository);
    }
    
}
