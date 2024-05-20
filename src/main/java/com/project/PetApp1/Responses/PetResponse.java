package com.project.PetApp1.Responses;

import lombok.Data;

import java.util.List;

@Data
public class PetResponse {
    Long id;
    String petName;
    Long userId;
    List<Long> posts;

    public PetResponse(Long id, String petName,Long userId, List<Long> posts) {
        this.id = id;
        this.petName = petName;
        this.userId = userId;
        this.posts = posts;
    }
}
