package com.project.PetApp1.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @ManyToOne(fetch = FetchType.EAGER)//post objesini çektiğinde user'ı hemen çek demek
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)//bir user silindiğinde ilgili tüm postlarını sil
    User user;

    @Lob
    String photo;

    @Lob
    @Column(columnDefinition = "text")
    String text;

    @Temporal(TemporalType.TIMESTAMP)
    Date createDate;
}
