package com.project.PetApp1.Responses;


import lombok.Data;

import java.util.List;

@Data
public class FollowedResponse {

    List<Long> followed;
    Long userId;
    public FollowedResponse(List<Long> followed, Long userId) {
        this.followed = followed;
        this.userId = userId;
    }
}
