package com.project.PetApp1.Responses;

import lombok.Data;

@Data
public class FollowResponse {
    Long id;
    String followerUserName;
    String followedUserName;
}
