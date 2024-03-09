package com.project.PetApp1.Responses;

import com.project.PetApp1.Requests.RequestStatus;
import lombok.Data;

@Data
public class FollowRequestResponse {

    private Long id;
    private Long userId;
    private String profilePhoto;
    private String username;
    private RequestStatus status;
}
