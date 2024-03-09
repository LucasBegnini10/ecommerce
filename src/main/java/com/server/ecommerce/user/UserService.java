package com.server.ecommerce.user;

import com.server.ecommerce.file.FileService;
import com.server.ecommerce.user.dto.UserRegisterDTO;
import com.server.ecommerce.user.dto.UserUpdateDTO;
import com.server.ecommerce.user.dto.UserUpdatePasswordDTO;
import com.server.ecommerce.user.exceptions.InvalidPasswordException;
import com.server.ecommerce.user.exceptions.UserNotFoundException;
import com.server.ecommerce.utils.FileUtils;
import com.server.ecommerce.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordUtils passwordUtils;

    private final FileService fileService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordUtils passwordUtils, FileService fileService){
        this.userRepository = userRepository;
        this.passwordUtils = passwordUtils;
        this.fileService = fileService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByEmail = userRepository.findByEmail(username);

        if(userByEmail.isEmpty()) throw new UsernameNotFoundException("User not found");
        return userByEmail.get();
    }


    public User findUserById(String id) throws UserNotFoundException {
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

        user.setId(UUID.randomUUID().toString());
        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPhone(userRegisterDTO.getPhone());
        user.setEnabled(true);
        user.setAccountExpired(false);
        user.setAccountLocked(false);
        user.setCredentialsExpired(false);
        user.setRole(RoleType.USER);
        user.setPassword(userRegisterDTO.getPassword());

        return user;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(String id, UserUpdateDTO userUpdateDTO){
        User user = this.findUserById(id);

        user.setName(userUpdateDTO.name());
        user.setPhone(userUpdateDTO.phone());

        return userRepository.save(user);
    }

    public void deleteUser(String id){
        User user = this.findUserById(id);

        user.setEnabled(false);

        userRepository.save(user);
    }

    public void updatePassword(User user, String password){
        String pwdHash = passwordUtils.hashPwd(password);

        user.setPassword(pwdHash);

        userRepository.save(user);
    }

    public void uploadImgPicture(String userId, MultipartFile multipartFile) throws IOException {
        User user = this.findUserById(userId);

        String key = user.getId() + "/picture_img.png";
        String url =  "https://" + System.getenv("CLOUD_FRONT_DOMAIN") + "/" + key;

        File file = FileUtils.convertMultipartFileToFile(multipartFile);

        fileService.save(key, file);

        file.delete();

        user.setPictureUrl(url);

        userRepository.save(user);
    }

    public void updatePassword(String userId, UserUpdatePasswordDTO userUpdatePasswordDTO){
        User user = findUserById(userId);

        if(!passwordUtils.comparePwd(userUpdatePasswordDTO.currentPassword(), user.getPassword())){
            throw new InvalidPasswordException();
        }

        updatePassword(user, userUpdatePasswordDTO.newPassword());
    }
}
