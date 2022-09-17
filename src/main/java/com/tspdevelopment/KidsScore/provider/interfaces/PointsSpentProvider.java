package com.tspdevelopment.KidsScore.provider.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tspdevelopment.KidsScore.data.model.PointsSpent;

public interface PointsSpentProvider extends BaseProvider<PointsSpent>{

    public PointsSpent create(PointsSpent newItem);

    public void delete(UUID id);

    public List<PointsSpent> findAll();

    public Optional<PointsSpent> findById(UUID id);

    public PointsSpent update(PointsSpent replaceItem, UUID id);

    public List<PointsSpent> search(PointsSpent item);
    
}
