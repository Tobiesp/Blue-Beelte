package com.tspdevelopment.bluebeetle.provider.interfaces;

import java.util.List;

import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import com.tspdevelopment.bluebeetle.data.model.PointType;

public interface PointTypeProvider extends BaseProvider<PointType>{

    public List<PointType> findByGroup(Group group);
    
    public List<PointType> findByCategory(PointCategory pointCategory);
    
    public List<PointType> findByCategoryAndGroup(PointCategory pointCategory, Group group);
    
}
