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
        User user = userService.getOneUser(newPostRequest.getUserId());
        if (user == null)
            return null;

        try {
            Post toSave = new Post();
            toSave.setId(newPostRequest.getId());
            toSave.setText(newPostRequest.getText());

            if (photo != null && !photo.isEmpty()) {
                toSave.setPhoto(photo.getBytes());
            }

            toSave.setUser(user);
            return postRepository.save(toSave);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Post updateOnePostById(Long postId, PostUpdateRequest updatePostRequest, MultipartFile photo) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post postToUpdate = postOptional.get();

            // Güncelleme isteğindeki verileri kontrol et
            if (updatePostRequest.getText() != null) {
                postToUpdate.setText(updatePostRequest.getText());
            }


            try {
                // Fotoğraf güncelleme isteği varsa
                if (photo != null && !photo.isEmpty()) {
                    postToUpdate.setPhoto(photo.getBytes());
                }

                return postRepository.save(postToUpdate);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null; // Post bulunamadı
        }
    }

    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
    }
}
