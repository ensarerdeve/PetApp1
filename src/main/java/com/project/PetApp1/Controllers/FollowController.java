package com.project.PetApp1.Controllers;

import com.project.PetApp1.Entities.Follow;
import com.project.PetApp1.Requests.FollowCreateRequest;
import com.project.PetApp1.Responses.FollowResponse;
import com.project.PetApp1.Services.FollowService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }


    @GetMapping
    public Set<FollowResponse> getAllFollows(){
        return followService.getAllFollows();
    }

    @GetMapping("/follows/{userId}")
    public Set<FollowResponse> getAllFollowsOfUser(@PathVariable Long userId){
        return followService.getAllFollowsOfUser(userId);
    }

    @GetMapping("/followers/{userId}")
    public Set<FollowResponse> getAllFollowersOfUser(@PathVariable Long userId){
        return followService.getAllFollowersOfUser(userId);
    }

    @PostMapping
    public Follow createFollow (@RequestBody FollowCreateRequest followCreateRequest){
        return followService.createFollow(followCreateRequest);
    }

    @DeleteMapping("/{followId}")
    public void deleteFollow(@PathVariable Long followId){
        followService.deleteFollowById(followId);
    }

}
