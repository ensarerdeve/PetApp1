package com.project.PetApp1.Services;

import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveOneUser(User newUser) {
        return userRepository.save(newUser);
    }

    public User getOneUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateOneUser(Long userId, User newUser) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){        //if there is user this will update users informations request body will bring us user
            User foundUser = user.get();
            foundUser.setUserName(newUser.getUserName());
            foundUser.setPassword(newUser.getPassword());
            foundUser.setMail(newUser.getMail());
            foundUser.setPhone(newUser.getPhone());
            foundUser.setName(newUser.getName());
            foundUser.setSurname(newUser.getSurname());
            userRepository.save(foundUser);
            return foundUser;
        }else {
            return null;

        }
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }
}
