package com.project.PetApp1.Requests;

import lombok.Data;

@Data
public class UserRegisterRequest {
    String userName;
    String password;
    String mail;
    Long phone;
    String name;
    String surname;
}
