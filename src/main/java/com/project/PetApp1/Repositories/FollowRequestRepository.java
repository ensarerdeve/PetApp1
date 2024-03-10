package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.FollowRequest;
import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Requests.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
    List<FollowRequest> findByFollowedUser(User user);

    Optional<FollowRequest> findByFollowerIdAndFollowedUserIdAndStatus(Long followerId, Long followedUserId, RequestStatus status);
}
