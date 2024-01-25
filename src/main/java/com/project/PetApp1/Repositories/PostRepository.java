package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
