package com.server.ecommerce.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }


    public Set<Role> getBasicRoles(){
        Set<String> basicRoles = new HashSet<>();
        basicRoles.add(RoleType.ROLE_USER.getRoleName());

        return roleRepository.findByNameIn(basicRoles);
    }

}
