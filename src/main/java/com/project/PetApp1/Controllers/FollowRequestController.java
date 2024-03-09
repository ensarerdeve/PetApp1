package com.project.PetApp1.Controllers;

import com.project.PetApp1.Entities.FollowRequest;
import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Responses.FollowRequestResponse;
import com.project.PetApp1.Services.FollowRequestService;
import com.project.PetApp1.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow/requests")
public class FollowRequestController {

    private final FollowRequestService followRequestService;

    private final UserService userService;

    public FollowRequestController(FollowRequestService followRequestService, UserService userService) {
        this.followRequestService = followRequestService ;
        this.userService = userService;
    }

    @GetMapping("/incoming/{userId}")
    public ResponseEntity<List<FollowRequestResponse>> getIncomingFollowRequests(@PathVariable Long userId) {
        User user = userService.getOneUserById(userId);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<FollowRequestResponse> incomingRequests = followRequestService.getIncomingFollowRequests(user);
        return ResponseEntity.ok(incomingRequests);
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

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Object> deleteFollowRequest(@PathVariable Long requestId) {
        try {
            followRequestService.cancelPendingFollowRequest(requestId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
