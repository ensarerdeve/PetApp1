package com.project.PetApp1.Models;
import jakarta.persistence.*;
import lombok.Data;

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
    private User user;
}
