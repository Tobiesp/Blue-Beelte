package com.tspdevelopment.bluebeetle.provider.interfaces;

import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import java.util.Optional;

/**
 *
 * @author tobiesp
 */
public interface PointCategoryProvider extends BaseProvider<PointCategory> {
    public Optional<PointCategory> findByCategory(String category);
}
