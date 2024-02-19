package com.project.PetApp1.Services;

import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.UserRepository;
import com.project.PetApp1.Requests.UserCreateRequest;
import com.project.PetApp1.Requests.UserUpdateRequest;
import com.project.PetApp1.Responses.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private String ppStorage = "C:\\Users\\aytug\\OneDrive\\Masaüstü\\foto";

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapUserToUserResponse)
                .collect(Collectors.toList());
    }

    public User saveOneUser(UserCreateRequest newUserRequest, MultipartFile photo) {
        User newUser = new User();
        newUser.setUserName(newUserRequest.getUserName());
        newUser.setMail(newUserRequest.getMail());
        newUser.setBio(newUserRequest.getBio());
        newUser.setName(newUserRequest.getName());
        newUser.setPhone(newUserRequest.getPhone());
        newUser.setSurname(newUserRequest.getSurname());
        newUser.setPassword(newUserRequest.getPassword());

        if (photo != null && !photo.isEmpty()) {
            String newPhotoPath = ppStorage + File.separator + photo.getOriginalFilename();
            transferFile(photo, newPhotoPath);
            newUser.setPhoto(newPhotoPath);
        }

        return userRepository.save(newUser);
    }


    public User getOneUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }


    public User updateOneUser(Long userId, UserUpdateRequest updateUserRequest, MultipartFile photo) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User foundUser = userOptional.get();


            foundUser.setUserName(updateUserRequest.getUserName());
            foundUser.setPassword(updateUserRequest.getPassword());
            foundUser.setMail(updateUserRequest.getMail());
            foundUser.setBio(updateUserRequest.getBio());
            foundUser.setPhone(updateUserRequest.getPhone());
            foundUser.setName(updateUserRequest.getName());
            foundUser.setSurname(updateUserRequest.getSurname());


            if (photo != null && !photo.isEmpty()) {

                if (foundUser.getPhoto() != null) {
                    File oldFile = new File(foundUser.getPhoto());
                    oldFile.delete();
                }

                String newPhotoPath = ppStorage + File.separator + photo.getOriginalFilename();
                transferFile(photo, newPhotoPath);
                foundUser.setPhoto(newPhotoPath);
            }

            userRepository.save(foundUser);

            return foundUser;
        } else {
            return null;
        }
    }


    private void transferFile(MultipartFile file, String destinationPath) {
        try {
            file.transferTo(new File(destinationPath));
        } catch (IOException e) {
            throw new RuntimeException("Dosya transferi sırasında bir hata oluştu.", e);
        }
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUserName(user.getUserName());
        userResponse.setMail(user.getMail());
        userResponse.setPassword(user.getPassword());
        userResponse.setPhone(user.getPhone());
        userResponse.setBio(user.getBio());
        userResponse.setName(user.getName());
        userResponse.setSurname(user.getSurname());
        userResponse.setPhoto(user.getPhoto());
        return userResponse;
    }
}

