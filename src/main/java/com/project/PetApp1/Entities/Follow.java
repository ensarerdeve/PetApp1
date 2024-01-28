package com.project.PetApp1.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@Entity
@Table(name = "follow")
public class Follow {

    @Id
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "followed_users", joinColumns = @JoinColumn(name = "follow_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> followed;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "follow_relationship",
            joinColumns = @JoinColumn(name = "follow_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> follower;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "followed_user_id")
    User followedUser;
}

