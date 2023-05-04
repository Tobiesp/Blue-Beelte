package com.tspdevelopment.bluebeetle.provider.interfaces;

import java.util.List;

import com.tspdevelopment.bluebeetle.data.model.Group;
import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import com.tspdevelopment.bluebeetle.data.model.PointType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointTypeProvider extends BaseProvider<PointType>{

    public List<PointType> findByGroup(Group group);
    
    public List<PointType> findByCategory(PointCategory pointCategory);
    
    public List<PointType> findByCategoryAndGroup(PointCategory pointCategory, Group group);

    public Page<PointType> findByGroup(Group group, Pageable pageable);
    
    public Page<PointType> findByCategory(PointCategory pointCategory, Pageable pageable);
    
    public Page<PointType> findByCategoryAndGroup(PointCategory pointCategory, Group group, Pageable pageable);
    
}
