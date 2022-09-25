package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tspdevelopment.kidsscore.data.model.Group;

public interface GroupProvider extends BaseProvider<Group> {

    public Group create(Group newItem);

    public void delete(UUID id);

    public List<Group> findAll();

    public Optional<Group> findById(UUID id);

    public Group update(Group replaceItem, UUID id);

    public List<Group> search(Group item);

    public Optional<Group> findByName(String name);

    public List<Group> findByNameLike(String name);
    
}
