package com.project.PetApp1.Chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    public void sendMessage(@RequestBody Map<String, String> requestBody) {
        String senderId = requestBody.get("senderId");
        String receiverId = requestBody.get("receiverId");
        String message = requestBody.get("message");
        chatService.sendMessage(senderId, receiverId, message);
    }
    @GetMapping("/{senderId}/{receiverId}")
    public List<Message> getMessagesBySenderAndReceiver(@PathVariable String senderId, @PathVariable String receiverId) throws ExecutionException, InterruptedException {
        return chatService.getMessagesBySenderAndReceiver(senderId, receiverId);
    }
    @GetMapping("/messages")
    public List<Message> getMessages() throws ExecutionException, InterruptedException {
        return chatService.getMessages();
    }

    @GetMapping("/messages/{userId1}/{userId2}")
    public List<Message> getMessagesBetweenUsers(@PathVariable String userId1, @PathVariable String userId2) throws ExecutionException, InterruptedException {
        return chatService.getMessagesBetweenUsers(userId1, userId2);
    }
    @GetMapping("/messages/{userId}")
    public List<Message> getMessagesByUser(@PathVariable String userId) throws ExecutionException, InterruptedException {
        return chatService.getMessagesByUser(userId);
    }
}
