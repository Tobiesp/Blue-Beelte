package com.tspdevelopment.bluebeetle.data.repository;

import com.tspdevelopment.bluebeetle.data.model.JWTSecret;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

/**
 *
 * @author tobiesp
 */
public interface JWTSecretRepository extends JpaRepository<JWTSecret, UUID> {
    public Optional<JWTSecret> findBySecretId(String secretId);

    public void deleteByCreatedAtBefore(LocalDateTime expireDate);
}
