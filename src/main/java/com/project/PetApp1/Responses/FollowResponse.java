package com.project.PetApp1.Responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FollowResponse {
    private Long id;
    private String followerUserName;
    private Long followerUserId;
    private String followedUserName;
    private Long followedUserId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;








}
