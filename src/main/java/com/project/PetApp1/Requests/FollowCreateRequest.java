package com.project.PetApp1.Requests;

import lombok.Data;

@Data
public class FollowCreateRequest {

    Long followerId;
    Long followedUserId;
}
