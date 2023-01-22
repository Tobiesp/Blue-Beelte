package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.UUID;

import com.tspdevelopment.kidsscore.data.model.User;

public interface UserProvider extends BaseProvider<User>{

    public User updatePassowrd(UUID id, String password);

    public void updateJwtTokenId(UUID userId, UUID tokenId);
}
