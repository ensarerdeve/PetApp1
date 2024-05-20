package com.project.PetApp1.Requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostUpdateRequest {
    String text;
    MultipartFile photo;
    List<Long> petIds;
}
