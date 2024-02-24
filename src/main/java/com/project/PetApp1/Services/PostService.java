package com.project.PetApp1.Services;

import com.project.PetApp1.Entities.*;
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

    @Autowired
    @Lazy
    private CommentService commentService;
    private LikeService likeService;
    private UserRepository userRepository;
    private FollowRepository followRepository;



    private String uploadDirectory = "C:\\Users\\aytug\\OneDrive\\Masaüstü\\foto";
    @Autowired
    public PostService(FollowRepository followRepository, PostRepository postRepository, UserService userService, CommentRepository commentRepository, CommentService commentService, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService =commentService;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    @Autowired
    public void setLikeService(LikeService likeService) {//like'ı tanımladık çünkü constructorda tanımladığımzda sonsuz döngüye giriyor
        this.likeService = likeService;
    }

    public List<PostResponse> getAllPosts() {
        List<Post> list = postRepository.findAll();

        return list.stream().map(p -> {
            List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));
            List<Comment> comments = commentRepository.findByPostId(p.getId());
            List<CommentResponse> commentResponses = comments.stream()
                    .map(comment -> new CommentResponse(comment.getId(), comment.getText(), comment.getUser().getUserName(), comment.getPost().getId()))
                    .collect(Collectors.toList());

            return new PostResponse(p, likes, commentResponses);
        }).collect(Collectors.toList());
    }

    public List<PostResponse> getOnePostByUserId(Long userId) {
        List<Post> list = postRepository.findByUserId(userId);

        return list.stream().map(p -> {
            List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));
            List<Comment> comments = commentRepository.findByUserId(p.getId());
            List<CommentResponse> commentResponses = comments.stream()
                    .map(comment -> new CommentResponse(comment.getId(), comment.getText(), comment.getUser().getUserName(), comment.getPost().getId()))
                    .collect(Collectors.toList());
            return new PostResponse(p, likes, commentResponses);
        }).collect(Collectors.toList());


    }

    public PostResponse getOnePostByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null),Optional.of(postId));
        List<CommentResponse> comments = commentService.getAllCommentsByPostId(postId);
        return new PostResponse(post,likes,comments);
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
                for (Post post : posts) {
                    List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.of(user.getId()), Optional.of(post.getId()));
                    List<CommentResponse> comments = commentService.getAllCommentsByPostId(post.getId());
                    postResponses.add(new PostResponse(post, likes, comments));
                }
                FollowedUsersResponse response = mapToFollowedUsersResponse(user, follow, postResponses);
                responses.add(response);
            }

            return responses;
        } else {
            throw new UserNotFoundException("Kullanıcı bulunamadı." + userId);
        }
    }

    public FollowedUsersResponse mapToFollowedUsersResponse(User user, Follow follow, List<PostResponse> posts) {
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

        return response;
    }

}








