package com.tspdevelopment.kidsscore.provider.interfaces;

import com.tspdevelopment.kidsscore.data.model.JWTSecret;
import java.util.Optional;
import java.time.LocalDateTime;

public interface JWTSecretProvider  extends BaseProvider<JWTSecret>{
    public Optional<JWTSecret> findBySecretId(String secretId);
    public void cleanUpSecret(LocalDateTime time);
}
