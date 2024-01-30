package com.project.PetApp1.Requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateRequest {

    MultipartFile photo;
    String userName;
    String mail;
    String password;
    String phone;
    String bio;
    String name;
    String surname;
}
