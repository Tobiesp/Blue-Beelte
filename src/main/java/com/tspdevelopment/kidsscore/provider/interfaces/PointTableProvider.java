package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.List;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.PointCategory;
import com.tspdevelopment.kidsscore.data.model.PointTable;

public interface PointTableProvider extends BaseProvider<PointTable>{

    public List<PointTable> findByGroup(Group group);
    
    public List<PointTable> findByPointCategory(PointCategory pointCategory);
    
}
