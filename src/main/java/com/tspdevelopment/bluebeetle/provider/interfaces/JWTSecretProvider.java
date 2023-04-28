package com.tspdevelopment.bluebeetle.provider.interfaces;

import com.tspdevelopment.bluebeetle.data.model.JWTSecret;
import java.util.Optional;
import java.time.LocalDateTime;

public interface JWTSecretProvider  extends BaseProvider<JWTSecret>{
    public Optional<JWTSecret> findBySecretId(String secretId);
    public void cleanUpSecret(LocalDateTime time);
}
