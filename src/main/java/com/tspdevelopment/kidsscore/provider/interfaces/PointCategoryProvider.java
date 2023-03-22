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
