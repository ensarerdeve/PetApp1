package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.Follow;
import com.project.PetApp1.Responses.FollowedResponse;
import com.project.PetApp1.Responses.FollowerResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<FollowedResponse> findFollowedByUserId(Long userId);
    List<FollowerResponse> findFollowersByUserId(Long userId);
}
