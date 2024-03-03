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

    private final FollowRequestService followRequestService;

    private final UserService userService;

    public FollowRequestController(FollowRequestService followRequestService, UserService userService) {
        this.followRequestService = followRequestService ;
        this.userService = userService;
    }

    @PostMapping("/accept/{userId}/{requestId}")
    public ResponseEntity<Object> acceptFollowRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        User user = userService.getOneUserById(userId);
        FollowRequest followRequest = followRequestService.getFollowRequestById(requestId);

        if (user == null || followRequest == null || !followRequest.getFollowedUser().equals(user)) {
            return ResponseEntity.badRequest().body("User or follow request not found.");
        }

        followRequestService.acceptFollowRequest(user, followRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject/{userId}/{requestId}")
    public ResponseEntity<Object> rejectFollowRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        User user = userService.getOneUserById(userId);
        FollowRequest followRequest = followRequestService.getFollowRequestById(requestId);

        if (user == null || followRequest == null || !followRequest.getFollowedUser().equals(user)) {
            return ResponseEntity.badRequest().body("User or follow request not found.");
        }

        followRequestService.rejectFollowRequest(user, followRequest);
        return ResponseEntity.ok().build();
    }
}
