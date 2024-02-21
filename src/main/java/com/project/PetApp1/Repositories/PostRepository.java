package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    List<Post> getOnePostByUserId(Long userId);
}
