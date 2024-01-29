package com.project.PetApp1.Requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserCreateRequest {

    MultipartFile photo;
    Long id;
    Long userId;
    String userName;
    String mail;
    String password;
    String phone;
    String bio;
    String name;
    String surname;
}
