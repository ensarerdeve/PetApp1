package com.project.PetApp1.Requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostUpdateRequest {
    String text;
    MultipartFile photo;
}
