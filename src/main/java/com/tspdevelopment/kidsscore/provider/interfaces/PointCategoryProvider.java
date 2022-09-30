/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tspdevelopment.kidsscore.provider.interfaces;

import com.tspdevelopment.kidsscore.data.model.PointCategory;
import java.util.Optional;

/**
 *
 * @author tobiesp
 */
public interface PointCategoryProvider extends BaseProvider<PointCategory> {
    public Optional<PointCategory> findByCategory(String category);
}
