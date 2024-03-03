package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.FollowRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
}
