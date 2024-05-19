package com.project.PetApp1.Services;

import com.project.PetApp1.Models.Comment;
import com.project.PetApp1.Models.Post;
import com.project.PetApp1.Models.User;
import com.project.PetApp1.Repositories.CommentRepository;
import com.project.PetApp1.Repositories.PostRepository;
import com.project.PetApp1.Requests.CommentCreateRequest;
import com.project.PetApp1.Requests.CommentUpdateRequest;
import com.project.PetApp1.Responses.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private UserService userService;
    private PostRepository postRepository;


    public CommentService(CommentRepository commentRepository, UserService userService, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    public List<CommentResponse> getAllCommentsWithParam() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getText(), comment.getUser().getUserName(), comment.getPost().getId()))
                .collect(Collectors.toList());
    }

    public CommentResponse getOneCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            String userName = comment.getUser().getUserName();
            Long postId = comment.getPost().getId();
            return new CommentResponse(comment.getId(), comment.getText(), userName, postId);
        } else {
            return null;
        }
    }

    public List<CommentResponse> getAllCommentsByPostId(Long postId){
        List<Comment> list = commentRepository.findByPostId(postId);
        return list.stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getText(), comment.getUser().getUserName(), comment.getPost().getId()))
                .collect(Collectors.toList());
    }

    public Comment createOneComment(CommentCreateRequest request) {
        User user = userService.getOneUserById(request.getUserId());
        Post post = postRepository.findById(request.getPostId()).orElse(null);
        if (user != null && post != null){
            Comment commentToSave = new Comment();
            commentToSave.setId(request.getId());
            commentToSave.setPost(post);
            commentToSave.setUser(user);
            commentToSave.setText(request.getText());
            commentToSave.setCreateDate(new Date());
            return commentRepository.save(commentToSave);
        }
        else
            return null;
    }

    public Comment updateOneCommentById(Long commentId, CommentUpdateRequest request) {
        Optional<Comment> comment = commentRepository.findById(commentId); //commentin varlığını sorguluyoruz.
        if (comment.isPresent()){
            Comment commentToUpdate = comment.get();
            commentToUpdate.setText(request.getText());
            commentToUpdate.setCreateDate(new Date());
            return commentRepository.save(commentToUpdate);

        }else
            return null;
    }

    public void deleteOneCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
