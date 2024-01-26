package com.project.PetApp1.Services;

import com.project.PetApp1.Entities.Post;
import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.PostRepository;
import com.project.PetApp1.Requests.PostCreateRequest;
import com.project.PetApp1.Requests.PostUpdateRequest;
import com.project.PetApp1.Responses.PostResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private PostRepository postRepository;
    private UserService userService;

    private final String PhotoPath = "C:\\Users\\aytug\\OneDrive\\Masaüstü\\foto";

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> list;
        if(userId.isPresent()) {
          list = postRepository.findByUserId(userId.get());
        }
        list = postRepository.findAll();
        return list.stream().map(p-> new PostResponse(p)).collect(Collectors.toList()); // post listesini dbden aldık ve onu postresponse listesine mapledik ve onu döndük

    }

    public Post getOnePostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post createOnePost(PostCreateRequest newPostRequest,  MultipartFile photo) {
        String photoPath = PhotoPath + photo.getOriginalFilename();
        User user = userService.getOneUserById(newPostRequest.getUserId());
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
