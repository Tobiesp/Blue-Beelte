package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tspdevelopment.kidsscore.data.model.RunningTotals;
import com.tspdevelopment.kidsscore.data.model.Student;

public interface RunningTotalsProvider extends BaseProvider<RunningTotals>{

    public RunningTotals create(RunningTotals newItem);

    public void delete(UUID id);

    public List<RunningTotals> findAll();

    public Optional<RunningTotals> findById(UUID id);

    public RunningTotals update(RunningTotals replaceItem, UUID id);

    public List<RunningTotals> search(RunningTotals item);

    public Optional<RunningTotals> findByStudent(Student student);
    
}
