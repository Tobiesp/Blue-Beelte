package com.tspdevelopment.bluebeetle.provider.interfaces;

import java.util.List;
import java.util.Optional;

import com.tspdevelopment.bluebeetle.data.model.ImportJob;

public interface ImportJobProvider extends BaseProvider<ImportJob> {

    public Optional<ImportJob> findByFileName(String name);

    public List<ImportJob> findByFileNameLike(String name);
    
}
