package com.project.PetApp1.Services;

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
    private FollowRequestService followRequestService;

    @Autowired
    public FollowService(FollowRepository followRepository, UserService userService, FollowRequestService followRequestService) {
        this.followRepository = followRepository;
        this.followRequestService = followRequestService;
        this.userService = userService;
    }

    private FollowResponse mapToResponse(Follow follow) {
        FollowResponse response = new FollowResponse();
        response.setId(follow.getId());
        response.setFollowedUserName(follow.getFollowedUser().getUserName());
        response.setFollowerUserName(follow.getFollower().getUserName());
        return response;
    }

    public FollowResponse createFollow(FollowCreateRequest followCreateRequest) {
        User follower = userService.getOneUserById(followCreateRequest.getFollowerId());
        User followedUser = userService.getOneUserById(followCreateRequest.getFollowedUserId());

        if (follower == null || followedUser == null) {
            throw new IllegalArgumentException("Follower or followed user not found.");
        }

        if (followedUser.isPrivate()) {
            // Eğer takip edilmek istenen kullanıcının profili kapalıysa, takip isteği gönderilir
            followRequestService.addFollowRequest(follower, followedUser);
            System.out.println("Follow request sent to user with id: " + followedUser.getId());
            return null; // Takip isteği gönderildiği için hemen bir yanıt dönülmeyebilir
        }

        // Takip işlemi gerçekleştir
        Follow followToSave = new Follow();
        followToSave.setFollowedUser(followedUser);
        followToSave.setFollower(follower);
        followToSave.setCreateDate(new Date());

        Follow savedFollow = followRepository.save(followToSave);

        if (savedFollow == null) {
            throw new IllegalStateException("Failed to save follow relationship.");
        }

        FollowResponse response = new FollowResponse();
        response.setId(savedFollow.getId());
        response.setFollowedUserName(savedFollow.getFollowedUser().getUserName());
        response.setFollowerUserName(savedFollow.getFollower().getUserName());

        return response;
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
