package com.server.ecommerce.recoveryPassword;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecoveryPasswordRepository extends JpaRepository<RecoveryPassword, UUID> {

    List<RecoveryPassword> findByUserIdAndIsEnabledTrue(UUID userId);

    Optional<RecoveryPassword> findByToken(String token);
}
