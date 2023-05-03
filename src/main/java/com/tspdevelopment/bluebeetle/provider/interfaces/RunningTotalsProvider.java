package com.tspdevelopment.bluebeetle.provider.interfaces;

import java.util.Optional;

import com.tspdevelopment.bluebeetle.data.model.RunningTotals;
import com.tspdevelopment.bluebeetle.data.model.Student;

public interface RunningTotalsProvider extends BaseProvider<RunningTotals>{

    public Optional<RunningTotals> findByStudent(Student student);
    
}
