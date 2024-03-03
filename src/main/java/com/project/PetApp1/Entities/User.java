package com.project.PetApp1.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Set;


@Entity
@Table(name="user")
@Data
@EqualsAndHashCode(exclude = {"following", "followed"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Size(min = 4, max = 50)
    @Column(name = "username", nullable = false, unique = true)
    String userName;

    @NotNull
    @Size(max = 255)
    @Pattern(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    String mail;

    @NotNull
    @Size(min = 6, max = 100)
    String password;

    @NotNull
    @Size(max = 20)
    String phone;

    @NotNull
    @Size(max = 100)
    String bio;

    @NotNull
    @Size(max = 50)
    String name;

    @NotNull
    @Size(max = 50)
    String surname;

    @NotNull
    String photo;

    @OneToMany(mappedBy = "follower", fetch = FetchType.EAGER)
    Set<Follow> following;

    @OneToMany(mappedBy = "followedUser", fetch = FetchType.EAGER)
    Set<Follow> followed;

    @Temporal(TemporalType.TIMESTAMP)
    Date createDate;

}