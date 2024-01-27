package com.server.ecommerce.user;

import com.server.ecommerce.role.RoleService;
import com.server.ecommerce.user.dto.UserRegisterDTO;
import com.server.ecommerce.user.dto.UserUpdateDTO;
import com.server.ecommerce.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
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


    public User findUserById(UUID id) throws UserNotFoundException {
        return userRepository.findByIdAndIsEnabledTrue(id).orElseThrow(UserNotFoundException::new);
    }

    public User findUserByEmail(String email)  throws UserNotFoundException {
        return userRepository.findByEmailAndIsEnabledTrue(email).orElseThrow(UserNotFoundException::new);
    }

    public List<User> findAll(){
        return userRepository.findAll();
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
        user.setPassword(userRegisterDTO.getPassword());

        return user;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(UUID id, UserUpdateDTO userUpdateDTO){
        User user = this.findUserById(id);

        user.setName(userUpdateDTO.name());
        user.setPhone(userUpdateDTO.phone());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id){
        User user = this.findUserById(id);

        user.setEnabled(false);

        userRepository.save(user);

    }
}
