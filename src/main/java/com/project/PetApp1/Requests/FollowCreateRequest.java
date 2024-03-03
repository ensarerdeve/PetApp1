package com.project.PetApp1.Requests;

import lombok.Data;

@Data
public class FollowCreateRequest {

    private Long followerId;
    private Long followedUserId;
}
