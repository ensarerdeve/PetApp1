package com.project.PetApp1.Responses;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class FollowedUsersResponse {
    private Long userId;
    private Set<FollowResponse> following;
    private List<PostResponse> posts;

}
