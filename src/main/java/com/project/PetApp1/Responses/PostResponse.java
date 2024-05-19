package com.project.PetApp1.Responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.PetApp1.Models.Pet;
import com.project.PetApp1.Models.Post;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostResponse {
    Long id;
    Long userId;
    String userName;
    String photo;
    String text;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    Date createDate;
    List<Long> petIds;
    List<String> petName;
    List<LikeResponse> postLikes;
    List<CommentResponse> comments;

    // Temel yapıcı, Post entity'sini kullanarak doldurur
    public PostResponse(Post entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getUserName();
        this.text = entity.getText();
        this.photo = entity.getPhoto();
        this.createDate = entity.getCreateDate();
        this.petIds = entity.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        this.petName = entity.getPets().stream().map(Pet::getPetName).collect(Collectors.toList());
    }

    // Beğeni ve yorum listeleriyle genişletilmiş yapıcı
    public PostResponse(Post entity, List<LikeResponse> likes, List<CommentResponse> comments) {
        this(entity); // Önceki yapılandırıcıyı çağırarak temel özellikleri set eder
        this.postLikes = likes;
        this.comments = comments;
    }

    // Tüm alanları içeren yapıcı
    public PostResponse(Post entity, List<LikeResponse> likes, List<CommentResponse> comments, List<PetResponse> petResponses) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getUserName();
        this.text = entity.getText();
        this.photo = entity.getPhoto();
        this.createDate = entity.getCreateDate();
        this.petIds = petResponses.stream().map(PetResponse::getId).collect(Collectors.toList());
        this.petName = petResponses.stream().map(PetResponse::getPetName).collect(Collectors.toList());
        this.postLikes = likes;
        this.comments = comments;
    }
}
