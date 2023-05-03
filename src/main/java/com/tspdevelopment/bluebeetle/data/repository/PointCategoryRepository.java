package com.tspdevelopment.bluebeetle.data.repository;

import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author tobiesp
 */
public interface PointCategoryRepository  extends JpaRepository<PointCategory, UUID> {
    public Optional<PointCategory> findByCategory(String category);
}
