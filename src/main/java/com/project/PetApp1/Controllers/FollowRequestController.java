package com.project.PetApp1.Controllers;

import com.project.PetApp1.Entities.FollowRequest;
import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Services.FollowRequestService;
import com.project.PetApp1.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow/requests")
public class FollowRequestController {

    private final FollowRequestService requestService;

    private final UserService userService;

    public FollowRequestController(FollowRequestService requestService, UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    @PostMapping("/accept/{userId}/{requestId}")
    public ResponseEntity<String> acceptFollowRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        User user = userService.getOneUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        FollowRequest followRequest = user.getFollowRequests().stream()
                .filter(req -> req.getId().equals(requestId))
                .findFirst()
                .orElse(null);

        if (followRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Follow request not found");
        }

        requestService.acceptFollowRequest(user, followRequest);
        return ResponseEntity.ok("Follow request accepted");
    }

    @PostMapping("/reject/{userId}/{requestId}")
    public ResponseEntity<String> rejectFollowRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        User user = userService.getOneUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        FollowRequest followRequest = user.getFollowRequests().stream()
                .filter(req -> req.getId().equals(requestId))
                .findFirst()
                .orElse(null);

        if (followRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Follow request not found");
        }

        requestService.rejectFollowRequest(user, followRequest);
        return ResponseEntity.ok("Follow request rejected");
    }

    @PostMapping("/add/{followerId}/{followedUserId}")
    public ResponseEntity<String> addFollowRequest(@PathVariable Long followerId, @PathVariable Long followedUserId) {
        User follower = userService.getOneUserById(followerId);
        User followedUser = userService.getOneUserById(followedUserId);
        if (follower == null || followedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        requestService.addFollowRequest(follower, followedUser);
        return ResponseEntity.ok("Follow request added");
    }

    @PostMapping("/remove/{userId}/{requestId}")
    public ResponseEntity<String> removeFollowRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        User user = userService.getOneUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        FollowRequest followRequest = user.getFollowRequests().stream()
                .filter(req -> req.getId().equals(requestId))
                .findFirst()
                .orElse(null);

        if (followRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Follow request not found");
        }

        requestService.removeFollowRequest(user, followRequest);
        return ResponseEntity.ok("Follow request removed");
    }
}
