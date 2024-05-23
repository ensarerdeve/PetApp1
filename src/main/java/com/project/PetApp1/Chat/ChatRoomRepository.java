package com.project.PetApp1.Chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomModel, String> {
     Optional<ChatRoomModel> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
