package com.project.PetApp1.Controllers;

import com.project.PetApp1.Requests.FollowCreateRequest;
import com.project.PetApp1.Responses.FollowResponse;
import com.project.PetApp1.Services.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public FollowResponse createFollow (@RequestBody FollowCreateRequest followCreateRequest){
        return followService.createFollow(followCreateRequest);
    }

    @DeleteMapping("/delete/{followerId}/{followedUserId}")
    public ResponseEntity<Object> deleteFollow(@PathVariable Long followerId, @PathVariable Long followedUserId){
        try {
            followService.deleteFollowById(followerId, followedUserId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
