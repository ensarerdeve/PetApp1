package com.project.PetApp1.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "post")
public class Post {
    @Id
    Long id;
    Long postId;
    String title;
    @Lob
    @Column(columnDefinition = "text")
    String text;
}
