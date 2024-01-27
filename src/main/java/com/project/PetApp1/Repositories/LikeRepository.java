package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUserIdAndPostId(Long userId, Long postId);

    List<Like> findByUserId(Long userId);

    List<Like> findByPostId(Long postId);
    //List<Object> findUserLikesByPostId(@Param("postIds") List<Long> postIds);
}
