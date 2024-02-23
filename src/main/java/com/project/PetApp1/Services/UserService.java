package com.project.PetApp1.Services;

import com.project.PetApp1.Entities.Follow;
import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.FollowRepository;
import com.project.PetApp1.Repositories.UserRepository;
import com.project.PetApp1.Requests.UserCreateRequest;
import com.project.PetApp1.Requests.UserUpdateRequest;
import com.project.PetApp1.Responses.FollowResponse;
import com.project.PetApp1.Responses.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private String ppStorage = "C:\\Users\\burak\\OneDrive\\Masaüstü\\source";

    private UserRepository userRepository;
    private FollowRepository followRepository;

    public UserService(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponse> responses = new ArrayList<>();
        for (User user : users) {
            Set<Follow> follows = followRepository.findByFollowerId(user.getId());
            Set<Follow> followers = followRepository.findByFollowedUserId(user.getId());
            responses.add(mapToResponse(user, follows, followers));
        }

        return responses;
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

    public List<UserResponse> getUserDataById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<Follow> follows = followRepository.findByFollowerId(user.getId());
            Set<Follow> followers = followRepository.findByFollowedUserId(user.getId());
            return Collections.singletonList(mapToResponse(user, follows, followers));
        } else {
            return Collections.emptyList();
        }
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

    public UserResponse mapToResponse(User user, Set<Follow> follows, Set<Follow> followers) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUserName(user.getUserName());
        response.setMail(user.getMail());
        response.setPassword(user.getPassword());
        response.setPhone(user.getPhone());
        response.setBio(user.getBio());
        response.setName(user.getName());
        response.setSurname(user.getSurname());
        response.setPhoto(user.getPhoto());

        Set<FollowResponse> followingResponse = follows.stream()
                .map(follow -> {
                    FollowResponse followResponse = new FollowResponse();
                    followResponse.setId(follow.getId());
                    followResponse.setFollowerUserName(follow.getFollower().getUserName());
                    followResponse.setFollowedUserName(follow.getFollowedUser().getUserName());
                    return followResponse;
                })
                .collect(Collectors.toSet());

        Set<FollowResponse> followerResponse = followers.stream()
                .map(follow -> {
                    FollowResponse followResponse = new FollowResponse();
                    followResponse.setId(follow.getId());
                    followResponse.setFollowerUserName(follow.getFollower().getUserName());
                    followResponse.setFollowedUserName(follow.getFollowedUser().getUserName());
                    return followResponse;
                })
                .collect(Collectors.toSet());

        response.setFollowing(followingResponse);
        response.setFollowers(followerResponse);

        return response;
    }

    public User getOneUserByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }
}

