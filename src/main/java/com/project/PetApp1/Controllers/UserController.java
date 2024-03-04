package com.project.PetApp1.Controllers;

import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Exceptions.UserNotFoundException;
import com.project.PetApp1.Requests.UserCreateRequest;
import com.project.PetApp1.Requests.UserPrivacyUpdateRequest;
import com.project.PetApp1.Requests.UserUpdateRequest;
import com.project.PetApp1.Responses.UserResponse;
import com.project.PetApp1.Responses.UserSearchResponse;
import com.project.PetApp1.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> createUser(@ModelAttribute UserCreateRequest newUserRequest, @RequestPart("photo") MultipartFile photo) {
        User createdUser = userService.saveOneUser(newUserRequest, photo);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public List<UserResponse> getOneUser(@PathVariable Long userId) {
        return userService.getUserDataById(userId);
    }

    @GetMapping("/activity/{userId}")
    public List<Object> getUserActivity(@PathVariable Long userId) {
        return userService.getUserActivity(userId);
    }

    @GetMapping("/search/{username}")
    public List<UserSearchResponse> searchUsersByUsername(@PathVariable String username) {
        return userService.searchUsersByUsername(username);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateOneUser(
            @PathVariable Long userId,
            @ModelAttribute UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        User updatedUser = userService.updateOneUser(userId, userUpdateRequest, photo);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{userId}/privacy")
    public ResponseEntity<Object> updateUserPrivacy(@PathVariable Long userId, @RequestBody UserPrivacyUpdateRequest privacyUpdateRequest) {
        boolean newPrivacySetting = privacyUpdateRequest.isProfileLock();

        try {
            userService.updateUserPrivacy(userId, newPrivacySetting);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating user privacy.");
        }
    }



    @DeleteMapping("/{userId}")
    public void deleteOneUser(@PathVariable Long userId) {
        userService.deleteById(userId);
    }
}



