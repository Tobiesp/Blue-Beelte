package com.tspdevelopment.bluebeetle.provider.interfaces;

import java.util.List;
import java.util.Optional;

import com.tspdevelopment.bluebeetle.data.model.Group;

public interface GroupProvider extends BaseProvider<Group> {

    public Optional<Group> findByName(String name);

    public List<Group> findByNameLike(String name);
    
}
