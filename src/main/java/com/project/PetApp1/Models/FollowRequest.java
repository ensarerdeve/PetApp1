package com.project.PetApp1.Models;

import com.project.PetApp1.Requests.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "follow_requests")
@Data
public class FollowRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_user_id", referencedColumnName = "id")
    private User followedUser;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

}
