package com.project.PetApp1.Chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chatRoom")
@Data
public class ChatRoomModel {
    @Id
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
}
