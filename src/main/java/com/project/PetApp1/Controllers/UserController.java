package com.project.PetApp1.Controllers;

import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping // for listing all of users
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping  //for creating new user
    public User createUser(@RequestBody User newUser){
        return userRepository.save(newUser);
    }

    @GetMapping("/{userId}")
    public User getOneUser(@PathVariable Long userId){
        //custom exception
        return userRepository.findById(userId).orElse(null);
    }

    @PutMapping("/{userId}")
    public User updateOneUser(@PathVariable Long userId, @RequestBody User newUser){
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){        //if there is user this will update users informations request body will bring us user
            User foundUser = user.get();
            foundUser.setUserName(newUser.getUserName());
            foundUser.setPassword(newUser.getPassword());
            userRepository.save(foundUser);
            return foundUser;
        }else {
            return null;

        }
    }

    @DeleteMapping("/{userId}")
    public void deleteOneUser(@PathVariable Long userId){   //pathvariable helps us to get id of user
        userRepository.deleteById(userId);
    }
}
