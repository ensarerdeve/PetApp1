package com.project.PetApp1.Services;

import com.project.PetApp1.Models.*;
import org.hibernate.Hibernate;
import com.project.PetApp1.Exceptions.UserNotFoundException;
import com.project.PetApp1.Repositories.*;
import com.project.PetApp1.Requests.PostCreateRequest;
import com.project.PetApp1.Requests.PostUpdateRequest;
import com.project.PetApp1.Responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private PostRepository postRepository;
    private UserService userService;
    private CommentRepository commentRepository;
    private PetRepository petRepository;

    @Autowired
    @Lazy
    private CommentService commentService;
    private LikeService likeService;
    private UserRepository userRepository;
    private FollowRepository followRepository;




    private String uploadDirectory = "C:\\src\\projects\\source";
    @Autowired
    public PostService(PetRepository petRepository, FollowRepository followRepository, PostRepository postRepository, UserService userService, CommentRepository commentRepository, CommentService commentService, UserRepository userRepository) {
        this.commentRepository = commentRepository;

        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService =commentService;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.petRepository = petRepository;
    }

    @Autowired
    public void setLikeService(LikeService likeService) {//like'ı tanımladık çünkü constructorda tanımladığımzda sonsuz döngüye giriyor
        this.likeService = likeService;
    }

    /*public List<PostResponse> getAllPosts() {
        List<Post> list = postRepository.findAll();

        return list.stream().map(p -> {
            List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));
            List<Comment> comments = commentRepository.findByPostId(p.getId());
            List<CommentResponse> commentResponses = comments.stream()
                    .map(comment -> new CommentResponse(comment.getId(), comment.getText(), comment.getUser().getUserName(), comment.getPost().getId()))
                    .collect(Collectors.toList());

            return new PostResponse(p, likes, commentResponses);
        }).collect(Collectors.toList());
    }*/

    public List<PostResponse> getOnePostByUserId(Long userId) {
        List<Post> list = postRepository.findByUserId(userId);

        return list.stream().map(p -> {
            List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));
            List<Comment> comments = commentRepository.findByUserId(p.getId());
            List<CommentResponse> commentResponses = comments.stream()
                    .map(comment -> new CommentResponse(comment.getId(), comment.getText(), comment.getUser().getUserName(), comment.getPost().getId()))
                    .collect(Collectors.toList());
            List<Pet> pets = p.getPets();
            List<PetResponse> petResponses = pets.stream()
                    .map(pet -> new PetResponse(pet.getId(), pet.getPetName(), pet.getUser().getId(), null))
                    .collect(Collectors.toList());

            return new PostResponse(p, likes, commentResponses, petResponses);
        }).collect(Collectors.toList());
    }



    public PostResponse getOnePostByPostId(Long postId) {
        // Belirtilen posta ait veriyi al, yoksa null döndür
        Post post = postRepository.findById(postId).orElse(null);

        if (post != null && post.getPets() != null) {
            // Postun beğenilerini al, varsa beğenileri getir, yoksa boş liste oluştur
            List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));

            // Postun yorumlarını al, varsa yorumları getir, yoksa boş liste oluştur
            List<CommentResponse> comments = commentService.getAllCommentsByPostId(postId);

            // Postun petlerini al, varsa petleri getir, yoksa boş liste oluştur
            List<PetResponse> petResponses = new ArrayList<>();

            for (Pet pet : post.getPets()) {
                // Debug logları ekleyin
                System.out.println("Pet ID: " + pet.getId());
                System.out.println("Pet Name: " + pet.getPetName());
                System.out.println("User ID: " + pet.getUser().getId());
                System.out.println("Post IDs: " + pet.getPosts().stream().map(Post::getId).collect(Collectors.toList()));

                petResponses.add(new PetResponse(
                        pet.getId(),
                        pet.getPetName(),
                        pet.getUser().getId(),
                        pet.getPosts().stream().map(Post::getId).collect(Collectors.toList())
                ));
            }

            // Post yanıtını oluştur ve döndür
            return new PostResponse(post, likes, comments, petResponses);
        } else {
            return null;
        }
    }




    public PostResponse createOnePost(PostCreateRequest newPostRequest, MultipartFile media) throws IOException {
        User user = userService.getOneUserById(newPostRequest.getUserId());
        if (user != null) {
            String mediaPath = uploadDirectory + File.separator + media.getOriginalFilename();
            media.transferTo(new File(mediaPath));

            List<Pet> pets = new ArrayList<>();
            for (Long id : newPostRequest.getPetId()) {
                Pet pet = petRepository.findById(id).orElse(null);
                if (pet != null) {
                    pets.add(pet);
                } else {
                    return null;
                }
            }
            Post postToSave = new Post();
            postToSave.setText(newPostRequest.getText());
            postToSave.setUser(user);
            postToSave.setPhoto(mediaPath);
            postToSave.setPets(pets);
            postToSave.setCreateDate(new Date());
            for (Pet pet : pets) {
                pet.getPosts().add(postToSave);
                postRepository.save(postToSave);
            }
            return new PostResponse(postToSave);
        } else {
            return null; // User not found
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
            if (postUpdateRequest.getPetIds() != null && !postUpdateRequest.getPetIds().isEmpty()) {
                List<Pet> pets = new ArrayList<>();
                for (Long id : postUpdateRequest.getPetIds()) {
                    Pet pet = petRepository.findById(id).orElse(null);
                    if (pet != null) {
                        pets.add(pet);
                    } else {
                        return null;
                    }
                }
                postToUpdate.setPets(pets);
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

    public List<FollowedUsersResponse> getPostsOfUsersFollows(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<Follow> follows = followRepository.findByFollowerId(user.getId());

            List<FollowedUsersResponse> responses = new ArrayList<>();
            for (Follow follow : follows) {
                List<PostResponse> postResponses = new ArrayList<>();
                User followedUser = follow.getFollowedUser();
                List<Post> posts = postRepository.findByUserId(followedUser.getId());
                List<PetResponse> petResponses = null;
                for (Post innerPost : posts) {
                    List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.of(user.getId()),
                            Optional.of(innerPost.getId()));
                    List<CommentResponse> comments = commentService.getAllCommentsByPostId(innerPost.getId());
                    List<Pet> pets = petRepository.findByUserId(followedUser.getId());
                    petResponses = new ArrayList<>();
                    for (Pet pet : pets) {
                        petResponses.add(new PetResponse(pet.getId(), pet.getPetName(), pet.getUser().getId(),
                                pet.getPosts().stream().map(post -> post.getId()).collect(Collectors.toList())));
                    }
                    postResponses.add(new PostResponse(innerPost, likes, comments, petResponses));
                }
                FollowedUsersResponse response = mapToFollowedUsersResponse(user, follow, postResponses, petResponses);
                responses.add(response);
            }
            return responses;
        } else {
            throw new UserNotFoundException("Kullanıcı bulunamadı. ID: " + userId);
        }
    }

    public FollowedUsersResponse mapToFollowedUsersResponse(User user, Follow follow, List<PostResponse> posts, List<PetResponse> pets) {
        FollowedUsersResponse response = new FollowedUsersResponse();
        response.setUserId(user.getId());

        Set<FollowResponse> followResponses = new HashSet<>();
        FollowResponse followResponse = new FollowResponse();
        followResponse.setId(follow.getId());
        followResponse.setFollowerUserName(follow.getFollower().getUserName());
        followResponse.setFollowedUserName(follow.getFollowedUser().getUserName());
        followResponses.add(followResponse);

        response.setFollowing(followResponses);
        response.setPosts(posts);
        response.setPets(pets);

        return response;
    }

}








