package com.project.PetApp1.Services;

import com.project.PetApp1.Entities.Post;
import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.PostRepository;
import com.project.PetApp1.Requests.PostCreateRequest;
import com.project.PetApp1.Requests.PostUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private PostRepository postRepository;
    private UserService userService;

    private final String PhotoPath = "C:\\Users\\aytug\\OneDrive\\Masaüstü\\foto";

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<Post> getAllPosts(Optional<Long> userId) {
        if(userId.isPresent())
            return postRepository.findByUserId(userId.get());
        return postRepository.findAll();

    }

    public Post getOnePostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post createOnePost(PostCreateRequest newPostRequest,  MultipartFile photo) {
        String photoPath = PhotoPath + photo.getOriginalFilename();
        User user = userService.getOneUser(newPostRequest.getUserId());
        if (user == null)
            return null;

        Post toSave = new Post();
        toSave.setId(newPostRequest.getId());
        toSave.setText(newPostRequest.getText());

        if (photo != null && !photo.isEmpty()) {
            toSave.setPhoto(photoPath);
        }

        toSave.setUser(user);
        return postRepository.save(toSave);
    }


    public Post updateOnePostById(Long postId, PostUpdateRequest updatePostRequest, MultipartFile photo) {
        String photoPath = PhotoPath + photo.getOriginalFilename();
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post postToUpdate = postOptional.get();

            // Güncelleme isteğindeki verileri kontrol et
            if (updatePostRequest.getText() != null) {
                postToUpdate.setText(updatePostRequest.getText());
            }


            // Fotoğraf güncelleme isteği varsa
            if (photo != null && !photo.isEmpty()) {
                postToUpdate.setPhoto(photoPath);
            }

            return postRepository.save(postToUpdate);
        } else {
            return null; // Post bulunamadı
        }
    }

    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
    }
}
