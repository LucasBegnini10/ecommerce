package com.server.ecommerce.user.recoveryPassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RecoveryPasswordRepository extends JpaRepository<RecoveryPassword, UUID> {


    List<RecoveryPassword> findByUserIdAndIsEnabledTrue(UUID userId);
}
