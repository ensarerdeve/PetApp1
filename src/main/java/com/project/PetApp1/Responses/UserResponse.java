package com.project.PetApp1.Responses;

import com.project.PetApp1.Entities.Follow;
import lombok.Data;

import java.util.Set;
@Data
public class UserResponse {
     Long id;
     String userName;
     String mail;
     String password;
     String phone;
     String bio;
     String name;
     String surname;
     String photo;
     Set<FollowResponse> followers;
     Set<FollowResponse> following;
}
