package com.tspdevelopment.bluebeetle.data.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tspdevelopment.bluebeetle.data.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author tobiesp
 */
public interface GroupRepository extends JpaRepository<Group, UUID> {
    public Optional<Group> findByName(String name);

    public List<Group> findByNameLike(String name);

    public Page<Group> findByNameLike(String name, Pageable pageable);
    
}
