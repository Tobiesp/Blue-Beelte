package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import com.tspdevelopment.bluebeetle.data.repository.PointCategoryRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.PointCategoryProvider;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.PointCategoryProviderImpl;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author tobiesp
 */
@Service
public class PointCategoryService extends BaseService<PointCategory, PointCategoryProvider> {

    public PointCategoryService(PointCategoryRepository repository) {
        this.provider = new PointCategoryProviderImpl(repository);
    }
    
    public PointCategory findByCategory(String category) {
        Optional<PointCategory> opc = this.provider.findByCategory(category);
        if(opc.isPresent()) {
            return opc.get();
        }
        return null;
    }
    
}
