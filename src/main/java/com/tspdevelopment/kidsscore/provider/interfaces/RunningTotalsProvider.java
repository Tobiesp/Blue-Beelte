package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.Optional;

import com.tspdevelopment.kidsscore.data.model.RunningTotals;
import com.tspdevelopment.kidsscore.data.model.Student;

public interface RunningTotalsProvider extends BaseProvider<RunningTotals>{

    public Optional<RunningTotals> findByStudent(Student student);
    
}
