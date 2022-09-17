package com.tspdevelopment.KidsScore.provider.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tspdevelopment.KidsScore.data.model.User;

public interface UserProvider extends BaseProvider<User>{
    
    public List<User> findAll();
    
    public Optional<User> findById(UUID id);

    public List<User> search(User item);
    
    public User update(User replaceItem, UUID id);
    
    public User create(User newItem);
    
    public void delete(UUID id);

    public User updatePassowrd(UUID id, String password);

    public void updateJwtTokenId(UUID userId, UUID tokenId);
}
