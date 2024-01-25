package com.server.ecommerce.user;

import com.server.ecommerce.role.RoleService;
import com.server.ecommerce.user.dto.UserRegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService){
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByEmail = userRepository.findByEmail(username);

        if(userByEmail.isEmpty()) throw new UsernameNotFoundException("User not found");

        return userByEmail.get();
    }

    public User buildUserRegister(UserRegisterDTO userRegisterDTO){
        User user = new User();

        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPhone(userRegisterDTO.getPhone());
        user.setEnabled(true);
        user.setAccountExpired(false);
        user.setAccountLocked(false);
        user.setCredentialsExpired(false);
        user.setRoles(roleService.getBasicRoles());

        return user;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }
}
