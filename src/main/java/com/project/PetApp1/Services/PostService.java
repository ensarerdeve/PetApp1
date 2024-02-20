package com.project.PetApp1.Services;

import com.project.PetApp1.Entities.Like;
import com.project.PetApp1.Entities.Post;
import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.LikeRepository;
import com.project.PetApp1.Repositories.PostRepository;
import com.project.PetApp1.Requests.PostCreateRequest;
import com.project.PetApp1.Requests.PostUpdateRequest;
import com.project.PetApp1.Responses.LikeResponse;
import com.project.PetApp1.Responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private PostRepository postRepository;
    private UserService userService;
    private LikeService likeService;



    private String uploadDirectory = "C:\\Users\\burak\\OneDrive\\Masaüstü\\source";
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Autowired
    public void setLikeService(LikeService likeService) {//like'ı tanımladık çünkü constructorda tanımladığımzda sonsuz döngüye giriyor
        this.likeService = likeService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> list;
        if(userId.isPresent()) {
          list = postRepository.findByUserId(userId.get());
        }else
            list = postRepository.findAll();
        return list.stream().map(p-> {
                List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));// postlar gelirken aynı zamanda o postun likelarını da getirdik
                return new PostResponse(p, likes);}).collect(Collectors.toList()); // post listesini dbden aldık ve onu postresponse listesine mapledik ve onu döndük

    }

    public Post getOnePostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }


    public PostResponse createOnePost(PostCreateRequest newPostRequest, MultipartFile media) throws IOException {
        User user = userService.getOneUserById(newPostRequest.getUserId());
        if (user != null) {
            String mediaPath = uploadDirectory + File.separator + media.getOriginalFilename();
            media.transferTo(new File(mediaPath)); // Dosyayı kopyala
            Post postToSave = new Post();
            postToSave.setText(newPostRequest.getText());
            postToSave.setUser(user);
            postToSave.setPhoto(mediaPath);
            Post savedPost = postRepository.save(postToSave);
            return new PostResponse(savedPost);
        } else {
            return null; // Kullanıcı bulunamadı
        }
    }

    private void transferFile(MultipartFile file, String destinationPath) { //uploadladığımız herhangi bir yerdeki fotoğrafı belirlerdiğimiz pathe getirmek için
        try {
            file.transferTo(new File(destinationPath));
        } catch (IOException e) {
            throw new RuntimeException("Dosya transferi sırasında bir hata oluştu.", e);
        }
    }


    public PostResponse updateOnePostById(Long postId, PostUpdateRequest postUpdateRequest, MultipartFile media) throws IOException {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post postToUpdate = postOptional.get();
            if (postUpdateRequest.getText() != null) {
                postToUpdate.setText(postUpdateRequest.getText());
            }
            if (media != null && !media.isEmpty()) {
                String mediaPath = uploadDirectory + File.separator + media.getOriginalFilename();
                media.transferTo(new File(mediaPath)); // Dosyayı kopyala
                postToUpdate.setPhoto(mediaPath);
            }
            Post updatedPost = postRepository.save(postToUpdate);
            return new PostResponse(updatedPost);
        } else {
            return null; // Gönderi bulunamadı
        }
    }

    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
    }
}
