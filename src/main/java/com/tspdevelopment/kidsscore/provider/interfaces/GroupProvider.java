package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.List;
import java.util.Optional;

import com.tspdevelopment.kidsscore.data.model.Group;

public interface GroupProvider extends BaseProvider<Group> {

    public Optional<Group> findByName(String name);

    public List<Group> findByNameLike(String name);
    
}
