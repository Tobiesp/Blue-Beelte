package com.tspdevelopment.bluebeetle.data.repository;

import com.tspdevelopment.bluebeetle.data.model.ImportJob;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author tobiesp
 */
public interface ImportJobRepository extends JpaRepository<ImportJob, UUID> {
    public Optional<ImportJob> findByFileName(String fileName);

    public List<ImportJob> findByFileNameLike(String fileName);
    
}
