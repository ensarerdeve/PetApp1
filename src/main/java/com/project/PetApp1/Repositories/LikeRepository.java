package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
