package com.project.PetApp1.Services;

import com.project.PetApp1.Models.*;
import com.project.PetApp1.Exceptions.UserNotFoundException;
import com.project.PetApp1.Repositories.*;
import com.project.PetApp1.Requests.PostCreateRequest;
import com.project.PetApp1.Requests.PostUpdateRequest;
import com.project.PetApp1.Responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${storage}")
    private String uploadDirectory;

    @Autowired
    public PostService(PetRepository petRepository, FollowRepository followRepository, PostRepository postRepository, UserService userService, CommentRepository commentRepository, CommentService commentService, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.petRepository = petRepository;
    }

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

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
                for (Post innerPost : posts) {
                    List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.of(user.getId()),
                            Optional.of(innerPost.getId()));
                    List<CommentResponse> comments = commentService.getAllCommentsByPostId(innerPost.getId());

                    List<Pet> pets = innerPost.getPets();
                    List<PetResponse> petResponses = pets.stream()
                            .map(pet -> new PetResponse(pet.getId(), pet.getPetName(), pet.getUser().getId(), null))
                            .collect(Collectors.toList());

                    postResponses.add(new PostResponse(innerPost, likes, comments, petResponses));
                }
                FollowedUsersResponse response = mapToFollowedUsersResponse(user, follow, postResponses, null);
                responses.add(response);
            }
            return responses;
        } else {
            throw new UserNotFoundException("Kullanıcı bulunamadı. ID: " + userId);
        }
    }

    public PostResponse getOnePostByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);

        if (post != null && post.getPets() != null) {
            List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));
            List<CommentResponse> comments = commentService.getAllCommentsByPostId(postId);
            List<PetResponse> petResponses = new ArrayList<>();

            for (Pet pet : post.getPets()) {
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
            return new PostResponse(post, likes, comments, petResponses);
        } else {
            return null;
        }
    }

    public PostResponse createOnePost(PostCreateRequest newPostRequest, MultipartFile media) throws IOException {
        User user = userService.getOneUserById(newPostRequest.getUserId());
        if (user != null) {
            Post postToSave = new Post();
            postToSave.setText(newPostRequest.getText());
            postToSave.setUser(user);
            postToSave.setCreateDate(new Date());

            List<Pet> pets = new ArrayList<>();
            for (Long id : newPostRequest.getPetId()) {
                Pet pet = petRepository.findById(id).orElse(null);
                if (pet != null) {
                    pets.add(pet);
                } else {
                    return null;
                }
            }
            postToSave.setPets(pets);
            postRepository.save(postToSave);

            String uniqueFileName = postToSave.getId() + "_" + media.getOriginalFilename();
            String mediaPath = uploadDirectory + File.separator + uniqueFileName;
            media.transferTo(new File(mediaPath));
            postToSave.setPhoto("https://petdiary.net/app/" + uniqueFileName);

            postRepository.save(postToSave);

            for (Pet pet : pets) {
                pet.getPosts().add(postToSave);
                petRepository.save(pet);
            }

            return new PostResponse(postToSave);
        } else {
            return null;
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
                String uniqueFileName = postToUpdate.getId() + "_" + media.getOriginalFilename();
                String mediaPath = uploadDirectory + File.separator + uniqueFileName;
                media.transferTo(new File(mediaPath));
                postToUpdate.setPhoto("https://petdiary.net/app/" + uniqueFileName);
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
            return null;
        }
    }

    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
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

    public void deletePostsByPetId(Long petId) {
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            if (post.getPets().stream().anyMatch(pet -> pet.getId().equals(petId))) {
                postRepository.delete(post);
            }
        }
    }
}
