/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import com.tspdevelopment.bluebeetle.data.model.PointType;
import com.tspdevelopment.bluebeetle.data.repository.PointCategoryRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointTypeRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointCategoryProvider;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointTypeProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.PointCategoryProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.PointTypeProviderImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author tobiesp
 */
public class PointTypeService extends BaseService<PointType, PointTypeProvider> {
    
    private final PointCategoryProvider pointCategoryProvider;

    public PointTypeService(PointTypeRepository repository, PointCategoryRepository pointCategoryRepository) {
        this.provider = new PointTypeProviderImpl(repository);
        this.pointCategoryProvider = new PointCategoryProviderImpl(pointCategoryRepository);
    }
    
    public List<PointType> findByCategory(String category) {
        PointTypeProvider prov = (PointTypeProvider)this.provider;
        Optional<PointCategory> pc = pointCategoryProvider.findByCategory(category);
        if(pc.isPresent()) {
            List<PointType> ptList = prov.findByCategory(pc.get());
            return ptList;
        } else {
            return new ArrayList<>();
        }
    }
    
    public List<PointType> findByGroup(Group group) {
        return this.provider.findByGroup(group);
    }
    
    public List<PointType> findByCategoryAndGroup(PointCategory pointCategory, Group group) {
        return this.provider.findByCategoryAndGroup(pointCategory, group);
    }
    
    public Page<PointType> findByCategory(String category, Pageable pageable) {
        PointTypeProvider prov = (PointTypeProvider)this.provider;
        Optional<PointCategory> pc = pointCategoryProvider.findByCategory(category);
        if(pc.isPresent()) {
            Page<PointType> ptList = prov.findByCategory(pc.get(), pageable);
            return ptList;
        } else {
            return null;
        }
    }
    
    public Page<PointType> findByGroup(Group group, Pageable pageable) {
        return this.provider.findByGroup(group, pageable);
    }
    
    public Page<PointType> findByCategoryAndGroup(PointCategory pointCategory, Group group, Pageable pageable) {
        return this.provider.findByCategoryAndGroup(pointCategory, group, pageable);
    }
    
}
