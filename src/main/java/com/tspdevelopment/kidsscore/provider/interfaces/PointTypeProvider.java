package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.List;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.PointCategory;
import com.tspdevelopment.kidsscore.data.model.PointType;

public interface PointTypeProvider extends BaseProvider<PointType>{

    public List<PointType> findByGroup(Group group);
    
    public List<PointType> findByCategory(PointCategory pointCategory);
    
    public List<PointType> findByCategoryAndGroup(PointCategory pointCategory, Group group);
    
}
