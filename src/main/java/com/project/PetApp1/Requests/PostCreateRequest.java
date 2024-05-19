package com.project.PetApp1.Requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostCreateRequest {
    MultipartFile photo;
    Long id;
    String text;
    Long userId;
    List<Long> petId;
}
