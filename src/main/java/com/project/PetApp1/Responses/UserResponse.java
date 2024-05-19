package com.project.PetApp1.Responses;

import lombok.Data;

import java.util.List;
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
     boolean profileLock;
     List<PetResponse> pets;
}
