package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.Follow;
import com.project.PetApp1.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
@Repository
public interface FollowRepository extends JpaRepository <Follow, Long> {
    Set<Follow> findByFollowerId(Long followerId);
    
    Set<Follow> findByFollowedUserId(Long userId);
}
