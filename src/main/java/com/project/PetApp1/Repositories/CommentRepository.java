package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
