package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tspdevelopment.kidsscore.data.model.User;

public interface UserProvider extends BaseProvider<User>{
    
    @Override
    public List<User> findAll();
    
    @Override
    public Optional<User> findById(UUID id);

    @Override
    public List<User> search(User item);
    
    @Override
    public User update(User replaceItem, UUID id);
    
    @Override
    public User create(User newItem);
    
    @Override
    public void delete(UUID id);

    public User updatePassowrd(UUID id, String password);

    public void updateJwtTokenId(UUID userId, UUID tokenId);
}
