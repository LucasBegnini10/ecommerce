package com.server.ecommerce.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Set<Role> findByNameIn(Set<String> roles);

}
