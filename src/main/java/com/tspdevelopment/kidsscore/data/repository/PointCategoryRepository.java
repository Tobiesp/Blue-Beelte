/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tspdevelopment.kidsscore.data.repository;

import com.tspdevelopment.kidsscore.data.model.PointCategory;
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
