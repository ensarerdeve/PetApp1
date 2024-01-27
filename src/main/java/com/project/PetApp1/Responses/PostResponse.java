package com.project.PetApp1.Responses;

import com.project.PetApp1.Entities.Like;
import com.project.PetApp1.Entities.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostResponse { //bazı belirlediğimiz verileri döndürmek için response kullandık.
    Long id;
    Long userId;
    String userName;
    String photo;
    String text;
    List<LikeResponse> postLikes;
    public PostResponse(Post entity, List<LikeResponse> likes){
        this.postLikes = likes;
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getUserName();
        this.text = entity.getText();
        this.photo = entity.getPhoto();

    }//amaç gelen post entitysini postresponse'a çevirmek
}
