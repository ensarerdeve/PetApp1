package com.project.PetApp1.Controllers;

import com.project.PetApp1.Entities.Post;
import com.project.PetApp1.Requests.PostCreateRequest;
import com.project.PetApp1.Requests.PostUpdateRequest;
import com.project.PetApp1.Services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public List<Post> getAllPosts(@RequestParam Optional<Long> userId){ //eğer user id gelirse idye göre postu verir eğer gelmezse default olarak hepsini getirir.
        return postService.getAllPosts(userId);

    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createOnePost(@ModelAttribute PostCreateRequest newPostRequest,
                                              @RequestPart(value = "photo", required = false) MultipartFile photo) {
        Post createdPost = postService.createOnePost(newPostRequest, photo);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }


    @GetMapping("/{postId}")
    public Post getOnePost(@PathVariable Long postId){
        return postService.getOnePostById(postId);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updateOnePost(
            @PathVariable Long postId,
            @ModelAttribute PostUpdateRequest updatePostRequest,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        Post updatedPost = postService.updateOnePostById(postId, updatePostRequest, photo);
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
