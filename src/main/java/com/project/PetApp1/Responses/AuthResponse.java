package com.project.PetApp1.Responses;

import lombok.Data;

@Data
public class AuthResponse {

    String message;
    Long userId;
    String accessToken;
    String refreshToken;
}
