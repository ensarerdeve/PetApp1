package com.project.PetApp1.Responses;

import lombok.Data;

import java.util.Date;

@Data
public class FollowResponse {
    private Long id;
    private String followerUserName;
    private Long followerUserId;
    private String followedUserName;
    private Long followedUserId;
    private Date createDate;
    private String formattedDate;







}
