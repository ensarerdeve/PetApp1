package com.project.PetApp1.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue()
    @Column(name = "id")
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)//post objesini çektiğinde user'ı hemen çekme demek
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)//bir user silindiğinde ilgili tüm postlarını sil
    @JsonIgnore
    User user;

    @Lob
    String photo;

    @Lob
    @Column(columnDefinition = "text")
    String text;
}
