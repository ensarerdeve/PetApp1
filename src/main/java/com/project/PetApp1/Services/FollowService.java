package com.project.PetApp1.Services;

import com.project.PetApp1.Entities.Follow;
import com.project.PetApp1.Entities.Like;
import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.FollowRepository;
import com.project.PetApp1.Requests.FollowCreateRequest;
import com.project.PetApp1.Responses.FollowedResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    private FollowRepository followRepository;
    private UserService userService;

    public FollowService(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    public List<FollowedResponse> getAllFollowsByUserId(Optional<Long> userId) {

         if (userId.isPresent()) {
            return followRepository.findFollowedByUserId(userId.get());
         }
         else
             return null;
        }

    public Follow createOneFollow(FollowCreateRequest request) {
        User user = userService.getOneUserById(request.getUserId());
        List<User> followedUsers =  userService.getUsersByIds(request.getFollowed());
        if(user != null && followedUsers != null) {
            Follow followToSave = new Follow();
            followToSave.setUser(user);
            followToSave.setFollowed(followedUsers);
            return followRepository.save(followToSave);
        }else
            return null;
    }
}
