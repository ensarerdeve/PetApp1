package com.project.PetApp1.Responses;

import lombok.Data;

@Data
public class CommentResponse {
    Long id;
    String comment;
    String userName;
    Long postId;

    public CommentResponse(Long id, String comment, String userName, Long postId) {
        this.id = id;
        this.comment = comment;
        this.userName = userName;
        this.postId = postId;
    }
}
