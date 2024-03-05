package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.FollowRequest;
import com.project.PetApp1.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
    List<FollowRequest> findByFollowedUser(User user);
}
