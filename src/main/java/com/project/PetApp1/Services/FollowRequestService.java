package com.project.PetApp1.Services;

import com.project.PetApp1.Entities.Follow;
import com.project.PetApp1.Entities.FollowRequest;
import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.FollowRepository;
import com.project.PetApp1.Repositories.UserRepository;
import com.project.PetApp1.Requests.RequestStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FollowRequestService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public FollowRequestService(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    public void acceptFollowRequest(User user, FollowRequest followRequest) {
        followRequest.setStatus(RequestStatus.ACCEPTED);
        user.getFollowRequests().remove(followRequest);
        Follow follow = new Follow();
        follow.setFollower(followRequest.getFollower());
        follow.setFollowedUser(user);
        follow.setCreateDate(new Date());
        followRepository.save(follow);
        userRepository.save(user);
    }

    public void rejectFollowRequest(User user, FollowRequest followRequest) {
        followRequest.setStatus(RequestStatus.REJECTED);
        user.getFollowRequests().remove(followRequest);
        userRepository.save(user);
    }

    public void addFollowRequest(User follower, User followedUser) {
        FollowRequest followRequest = new FollowRequest();
        followRequest.setFollower(follower);
        followRequest.setFollowedUser(followedUser);
        followRequest.setStatus(RequestStatus.PENDING);
        followedUser.getFollowRequests().add(followRequest);
        userRepository.save(followedUser);
    }

    public void removeFollowRequest(User user, FollowRequest followRequest) {
        user.getFollowRequests().remove(followRequest);
        userRepository.save(user);
    }
}

