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
    List<Long> petIds; // Pet bilgisi için yeni alanlar
    List<String> petName;
    List<LikeResponse> postLikes;
    List<CommentResponse> comments;


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

    public PostResponse(Post entity, List<LikeResponse> likes, List<CommentResponse> comments){
        this(entity); // Önceki yapılandırıcıyı çağırarak tekrar kod tekrarını önleriz
        this.postLikes = likes;
        this.comments = comments;
    }

    public PostResponse(Post p, List<LikeResponse> likes, List<CommentResponse> commentResponses, List<PetResponse> petResponses) {
    }
}
