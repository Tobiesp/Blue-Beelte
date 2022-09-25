package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.model.PointTotal;
import com.tspdevelopment.kidsscore.data.model.Student;

public interface PointTotalProvider extends BaseProvider<PointTotal>{

    public PointTotal create(PointTotal newItem);

    public void delete(UUID id);

    public List<PointTotal> findAll();

    public Optional<PointTotal> findById(UUID id);

    public PointTotal update(PointTotal replaceItem, UUID id);

    public List<PointTotal> search(PointTotal item);

    public List<PointTotal> findByGroup(Group group);

    public Optional<PointTotal> findByStudent(Student student);
    
}
