package com.project.PetApp1.Controllers;

import com.project.PetApp1.Entities.Post;
import com.project.PetApp1.Requests.PostCreateRequest;
import com.project.PetApp1.Requests.PostUpdateRequest;
import com.project.PetApp1.Responses.FollowedUsersResponse;
import com.project.PetApp1.Responses.PostResponse;
import com.project.PetApp1.Services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
    public class PostController {

    private PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping
    public List<PostResponse> getAllPosts(){
        return postService.getAllPosts();

    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> createOnePost(@ModelAttribute PostCreateRequest newPostRequest,
                                                      @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {
        PostResponse createdPost = postService.createOnePost(newPostRequest, photo);
        if (createdPost != null) {
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{userId}")
    public List<PostResponse> getOnePostByUserId(@PathVariable Long userId){
        return postService.getOnePostByUserId(userId);
    }

    @GetMapping("/id/{postId}")
    public PostResponse getOnePostByPostId(@PathVariable Long postId){
        return postService.getOnePostByPostId(postId);
    }

    @GetMapping("/follow/{userId}")
    public List<FollowedUsersResponse> getPostsOfUsersFollows(@PathVariable Long userId){
        return postService.getPostsOfUsersFollows(userId);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updateOnePost(
            @PathVariable Long postId,
            @ModelAttribute PostUpdateRequest updatePostRequest,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {
        PostResponse updatedPost = postService.updateOnePostById(postId, updatePostRequest, photo);
        if (updatedPost != null) {
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{postId}")
    public void deleteOnePost(@PathVariable Long postId){
        postService.deleteOnePostById(postId);
    }
    
    }
