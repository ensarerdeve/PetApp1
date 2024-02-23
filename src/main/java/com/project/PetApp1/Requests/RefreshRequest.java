package com.project.PetApp1.Requests;

import lombok.Data;

@Data
public class RefreshRequest {

    Long userId;
    String refreshToken;

}
