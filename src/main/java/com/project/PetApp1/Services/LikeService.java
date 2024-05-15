package com.project.PetApp1.Services;

import com.project.PetApp1.Models.Like;
import com.project.PetApp1.Models.Post;
import com.project.PetApp1.Models.User;
import com.project.PetApp1.Repositories.LikeRepository;
import com.project.PetApp1.Repositories.PostRepository;
import com.project.PetApp1.Requests.LikeCreateRequest;
import com.project.PetApp1.Responses.LikeResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private LikeRepository likeRepository;
    private UserService userService;
    private PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, UserService userService, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
        List<Like> list;
        if(userId.isPresent() && postId.isPresent()) {
            list = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
        }else if(userId.isPresent()) {
            list = likeRepository.findByUserId(userId.get());
        }else if(postId.isPresent()) {
            list = likeRepository.findByPostId(postId.get());
        }else
            list = likeRepository.findAll();
        return list.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());
    }

    public Like getOneLikeById(Long LikeId) {
        return likeRepository.findById(LikeId).orElse(null);
    }

    public Like createOneLike(LikeCreateRequest request) {
        User user = userService.getOneUserById(request.getUserId());
        Post post = postRepository.findById(request.getPostId()).orElse(null);
        if(user != null && post != null) {
            Like likeToSave = new Like();
            likeToSave.setId(request.getId());
            likeToSave.setPost(post);
            likeToSave.setUser(user);
            likeToSave.setCreateDate(new Date());
            return likeRepository.save(likeToSave);
        } else {
            return null;
        }
    }


    public void deleteOneLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }
}
