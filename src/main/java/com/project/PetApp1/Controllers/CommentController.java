package com.project.PetApp1.Controllers;

import com.project.PetApp1.Models.Comment;
import com.project.PetApp1.Requests.CommentCreateRequest;
import com.project.PetApp1.Requests.CommentUpdateRequest;
import com.project.PetApp1.Responses.CommentResponse;
import com.project.PetApp1.Services.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private CommentService commentService;

    public CommentController (CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentResponse> getAllComments(){
        return commentService.getAllCommentsWithParam();
    }

    @GetMapping("/{commentId}")
    public CommentResponse getOneComment(@PathVariable Long commentId){
        return commentService.getOneCommentById(commentId);
    }
    @GetMapping("/postId/{postId}")
    public List<CommentResponse> getCommentByPostId(@PathVariable Long postId){
        return commentService.getAllCommentsByPostId(postId);
    }

    @PostMapping
    public Comment createOneComment(@RequestBody CommentCreateRequest request){
        return commentService.createOneComment(request);
    }

    @PutMapping("/{commentId}")
    public Comment updateOneComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest request){
        return commentService.updateOneCommentById(commentId, request);
    }

    @DeleteMapping("/{commentId}")
    public void deleteOneComment(@PathVariable Long commentId){
        commentService.deleteOneCommentById(commentId);
    }

}
