package com.project.PetApp1.Responses;

import lombok.Data;

@Data
public class FollowerResponse {
    Long follower;

    public FollowerResponse(Long follower) {
        this.follower = follower;
    }

}
