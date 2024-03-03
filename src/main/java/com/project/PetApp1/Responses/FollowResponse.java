package com.project.PetApp1.Responses;

import lombok.Data;

@Data
public class FollowResponse {
    private Long id;
    private String followerUserName;
    private String followedUserName;


}
