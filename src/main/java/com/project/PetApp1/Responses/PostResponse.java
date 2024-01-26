package com.project.PetApp1.Responses;

import com.project.PetApp1.Entities.Post;
import lombok.Data;

@Data
public class PostResponse { //bazı belirlediğimiz verileri döndürmek için response kullandık.
    Long id;
    Long userId;
    String userName;
    String photo;
    String text;

    public PostResponse(Post entity){

        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getUserName();
        this.text = entity.getText();
        this.photo = entity.getPhoto();

    }//amaç gelen post entitysini postresponse'a çevirmek
}
