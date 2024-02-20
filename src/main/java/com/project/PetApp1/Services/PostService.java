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

    public PostResponse getOnePostByIdWithLikes(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));
        return new PostResponse(post, likes);
    }


    public Post createOnePost(PostCreateRequest newPostRequest, MultipartFile media) {
        User user = userService.getOneUserById(newPostRequest.getUserId());
        if (user == null) {
            // veya uygun bir hata mesajı
            return null;
        }

        String mediaPath = uploadDirectory + File.separator + media.getOriginalFilename();

        transferFile(media, mediaPath);

        Post toSave = new Post();
        toSave.setId(newPostRequest.getId());
        toSave.setText(newPostRequest.getText());
        toSave.setUser(user);

        if (media != null && !media.isEmpty()) {
            toSave.setPhoto(mediaPath);
        }

        return postRepository.save(toSave);
    }

    private void transferFile(MultipartFile file, String destinationPath) { //uploadladığımız herhangi bir yerdeki fotoğrafı belirlerdiğimiz pathe getirmek için
        try {
            file.transferTo(new File(destinationPath));
        } catch (IOException e) {
            throw new RuntimeException("Dosya transferi sırasında bir hata oluştu.", e);
        }
    }


    public Post updateOnePostById(Long postId, PostUpdateRequest updatePostRequest, MultipartFile photo) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post postToUpdate = postOptional.get();


            if (updatePostRequest.getText() != null) {
                postToUpdate.setText(updatePostRequest.getText());
            }

            // Fotoğraf güncelleme isteği varsa
            if (photo != null && !photo.isEmpty()) {
                // Yeni dosya adı oluştur
                String newPhotoPath = uploadDirectory + File.separator + photo.getOriginalFilename();

                // Dosyayı kopyala
                transferFile(photo, newPhotoPath);

                // Eski dosyayı sil (bunu yapıyoruz ki dosyalar fazla yer tutmasın update yaptıktan sonra eski dosyayı siliyor)
                if (postToUpdate.getPhoto() != null) {
                    File oldFile = new File(postToUpdate.getPhoto());
                    oldFile.delete();
                }

                // Yeni dosya adını güncelle
                postToUpdate.setPhoto(newPhotoPath);
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
