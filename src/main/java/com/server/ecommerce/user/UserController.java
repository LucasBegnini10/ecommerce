package com.server.ecommerce.user;

import com.server.ecommerce.infra.RestResponseHandler;
import com.server.ecommerce.user.dto.UserUpdateDTO;
import com.server.ecommerce.user.dto.UserUpdatePasswordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users" )
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getUserById() {
        return RestResponseHandler.generateResponse(
                "User list",
                HttpStatus.OK,
                userService.findAll()
        );
    }

    @GetMapping("{id}" )
    public ResponseEntity<Object> getUserById(@PathVariable UUID id) {
        return RestResponseHandler.generateResponse(
                "User with id " + id,
                HttpStatus.OK,
                userService.findUserById(id)
        );
    }

    @PutMapping("{id}" )
    public ResponseEntity<Object> updateUserById(@PathVariable UUID id, @RequestBody UserUpdateDTO userUpdateDTO) {
        return RestResponseHandler.generateResponse(
                "User updated with id " + id,
                HttpStatus.OK,
                userService.updateUser(id, userUpdateDTO)
        );
    }

    @DeleteMapping("{id}" )
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return RestResponseHandler.generateResponse(
                "User deleted with id " + id,
                HttpStatus.OK,
                null
        );
    }

    @PostMapping("/{userId}/picture" )
    public ResponseEntity<Object> uploadImgPicture(@PathVariable UUID userId, @RequestBody MultipartFile file) {
        try {
            userService.uploadImgPicture(userId, file);

            return RestResponseHandler.generateResponse(
                    "Profile picture updated!",
                    HttpStatus.OK,
                    null
            );
        } catch (IOException ex) {
            return RestResponseHandler.generateResponse(
                    "Error to save profile picture",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
        }
    }

    @PatchMapping("{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable UUID userId, @RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO){
        userService.updatePassword(userId, userUpdatePasswordDTO);


        return RestResponseHandler.generateResponse(
                "Password updated!",
                HttpStatus.OK,
                null
        );
    }
}
