package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.Follow;
import com.project.PetApp1.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface FollowRepository extends JpaRepository <Follow, Long> {
    Set<Follow> findByFollowerId(Long followerId);

    Follow findByFollowerAndFollowedUser(User follower, User followedUser);

    Set<Follow> findByFollowedUserId(Long userId);
}
