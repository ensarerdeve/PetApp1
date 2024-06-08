package com.project.PetApp1.Models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pet")
@Data
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String petName;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pet_posts",
    joinColumns = @JoinColumn(name = "pet_id"),
    inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> posts;
}
