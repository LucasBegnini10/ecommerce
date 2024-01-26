package com.server.ecommerce.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Set<Role> findByNameIn(Set<String> roles);

}
