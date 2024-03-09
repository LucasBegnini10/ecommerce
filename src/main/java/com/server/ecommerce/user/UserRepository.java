package com.server.ecommerce.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndIsEnabledTrue(String id);

    Optional<User> findByEmailAndIsEnabledTrue(String id);


}
