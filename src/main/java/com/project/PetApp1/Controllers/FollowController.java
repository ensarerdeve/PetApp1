package com.project.PetApp1.Controllers;

import com.project.PetApp1.Entities.Follow;
import com.project.PetApp1.Requests.FollowCreateRequest;
import com.project.PetApp1.Requests.LikeCreateRequest;
import com.project.PetApp1.Responses.FollowedResponse;
import com.project.PetApp1.Services.FollowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @GetMapping
    public List<FollowedResponse> getAllFollowsByUserId(@RequestParam Optional<Long> userId){//bir userın takip ettikleri
        return followService.getAllFollowsByUserId(userId);
    }

    @PostMapping
    public Follow createFollow(@RequestBody FollowCreateRequest request){
        return followService.createOneFollow(request);
    }
}
