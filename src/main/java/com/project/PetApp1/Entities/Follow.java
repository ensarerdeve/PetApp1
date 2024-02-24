package com.project.PetApp1.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "follow")
@Data
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    User follower;

    @ManyToOne
    @JoinColumn(name = "followed_user_id")
    User followedUser;


    @Temporal(TemporalType.TIMESTAMP)
    Date createDate;

}
