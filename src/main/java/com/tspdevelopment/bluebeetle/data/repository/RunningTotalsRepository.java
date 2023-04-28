package com.tspdevelopment.bluebeetle.data.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.bluebeetle.data.model.RunningTotals;
import com.tspdevelopment.bluebeetle.data.model.Student;

/**
 *
 * @author tobiesp
 */
public interface RunningTotalsRepository extends JpaRepository<RunningTotals, UUID> {
    public Optional<RunningTotals> findByStudent(Student student);
    
}
