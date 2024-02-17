package com.project.PetApp1.Controllers;

import com.project.PetApp1.Entities.Post;
import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.UserRepository;
import com.project.PetApp1.Requests.PostUpdateRequest;
import com.project.PetApp1.Requests.UserCreateRequest;
import com.project.PetApp1.Requests.UserUpdateRequest;
import com.project.PetApp1.Responses.UserResponse;
import com.project.PetApp1.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping // for listing all of users
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> createUser(@ModelAttribute UserCreateRequest newUserRequest, @RequestPart("photo") MultipartFile photo) {
        User createdUser = userService.saveOneUser(newUserRequest, photo);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public User getOneUser(@PathVariable Long userId){
        return userService.getOneUserById(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateOneUser(
            @PathVariable Long userId,
            @ModelAttribute UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        User updatedUser = userService.updateOneUser(userId,userUpdateRequest,photo);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    public void deleteOneUser(@PathVariable Long userId){   //pathvariable helps us to get id of user
        userService.deleteById(userId);
    }
}
