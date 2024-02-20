package com.project.PetApp1.Services;

import com.project.PetApp1.Entities.Comment;
import com.project.PetApp1.Entities.Follow;
import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.FollowRepository;
import com.project.PetApp1.Requests.FollowCreateRequest;
import com.project.PetApp1.Responses.FollowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FollowService {

    private FollowRepository followRepository;
    private UserService userService;

    @Autowired
    public FollowService(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    private FollowResponse mapToResponse(Follow follow) {
        FollowResponse response = new FollowResponse();
        response.setId(follow.getId());
        response.setFollowedUserName(follow.getFollowedUser().getUserName()); // Followed user'ın username'ini al
        response.setFollowerUserName(follow.getFollower().getUserName()); // Follower'ın username'ini al
        return response;
    }


    public Follow createFollow(FollowCreateRequest followCreateRequest) {
        User follower = (User) userService.getOneUserById(followCreateRequest.getFollowerId());
        User followedUser = (User) userService.getOneUserById(followCreateRequest.getFollowedUserId());

        // Veritabanında takip edilen kullanıcının mevcut olup olmadığını kontrol edin
        Follow existingFollow = followRepository.findByFollowerAndFollowedUser(follower, followedUser);
        if (existingFollow != null) {
            throw new IllegalArgumentException("Bu kullanıcıyı zaten takip ediyorsunuz.");
        }

        if (follower != null && followedUser != null){
            Follow followToSave = new Follow();
            followToSave.setFollowedUser(followedUser);
            followToSave.setFollower(follower);
            return followRepository.save(followToSave);
        } else {
            throw new IllegalArgumentException("Takip eden veya takip edilen kullanıcı bulunamadı.");
        }
    }

    public void deleteFollowById(Long followId) {
        followRepository.deleteById(followId);
    }

    public Set<FollowResponse> getAllFollows() {
        List<Follow> follows = followRepository.findAll();
        return follows.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<FollowResponse> getAllFollowsOfUser(Long userId) {
        Set<Follow> follows = followRepository.findByFollowerId(userId);
        return follows.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toSet());
    }

    public Set<FollowResponse> getAllFollowersOfUser(Long userId) {
        Set<Follow> followers = followRepository.findByFollowedUserId(userId);
        return followers.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toSet());
    }
}
