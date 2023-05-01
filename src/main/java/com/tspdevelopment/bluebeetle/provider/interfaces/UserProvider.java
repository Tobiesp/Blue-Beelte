package com.tspdevelopment.bluebeetle.provider.interfaces;

import java.util.UUID;

import com.tspdevelopment.bluebeetle.data.model.User;

public interface UserProvider extends BaseProvider<User>{

    public User updatePassword(UUID id, String password);

    public void updateJwtTokenId(UUID userId, UUID tokenId);
}
