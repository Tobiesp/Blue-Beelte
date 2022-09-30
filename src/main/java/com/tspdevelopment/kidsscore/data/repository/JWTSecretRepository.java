package com.tspdevelopment.kidsscore.data.repository;

import com.tspdevelopment.kidsscore.data.model.JWTSecret;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author tobiesp
 */
public interface JWTSecretRepository extends JpaRepository<JWTSecret, UUID> {
    public Optional<JWTSecret> findBySecretId(String secretId);
}
