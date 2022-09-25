package com.tspdevelopment.kidsscore.data.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.kidsscore.data.model.RunningTotals;
import com.tspdevelopment.kidsscore.data.model.Student;

/**
 *
 * @author tobiesp
 */
public interface RunningTotalsRepository extends JpaRepository<RunningTotals, UUID> {
    public Optional<RunningTotals> findByStudent(Student student);
    
}
