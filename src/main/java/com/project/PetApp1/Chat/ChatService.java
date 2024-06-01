package com.project.PetApp1.Chat;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import com.google.cloud.firestore.Firestore;

@Service
public class ChatService {

    private final Firestore db;
    private final Gson gson = new Gson();

    public ChatService() {
        String projectId = "chat-service1";
        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setProjectId(projectId)
                .build();
        db = firestoreOptions.getService();
    }

    public void sendMessage(String senderId, String receiverId, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("senderId", senderId);
        data.put("receiverId", receiverId);
        data.put("message", message);
        data.put("timestamp", System.currentTimeMillis());

        ApiFuture<DocumentReference> future = db.collection("messages").add(data);

        try {
            DocumentReference documentReference = future.get();
            System.out.println("Document added with ID: " + documentReference.getId());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error adding document: " + e.getMessage());
        }
    }
    public List<Message> getMessages() throws ExecutionException, InterruptedException {
        return db.collection("messages").get().get().getDocuments().stream()
                .map(doc -> {
                    Map<String, Object> messageData = doc.getData();
                    long timestamp = Long.parseLong(messageData.get("timestamp").toString());
                    Date date = new Date(timestamp);
                    return new Message(
                            (String) messageData.get("senderId"),
                            (String) messageData.get("receiverId"),
                            (String) messageData.get("message"),
                            date
                    );
                })
                .collect(Collectors.toList());
    }
    public List<Message> getMessagesBySenderAndReceiver(String senderId, String receiverId) throws ExecutionException, InterruptedException {
        return db.collection("messages")
                .whereEqualTo("senderId", senderId)
                .whereEqualTo("receiverId", receiverId)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> {
                    Map<String, Object> messageData = doc.getData();
                    long timestamp = Long.parseLong(messageData.get("timestamp").toString());
                    Date date = new Date(timestamp);
                    return new Message(
                            (String) messageData.get("senderId"),
                            (String) messageData.get("receiverId"),
                            (String) messageData.get("message"),
                            date
                    );
                })
                .collect(Collectors.toList());
    }
    public List<Message> getMessagesBetweenUsers(String userId1, String userId2) throws ExecutionException, InterruptedException {
        List<Message> messagesSentByUser1 = db.collection("messages")
                .whereEqualTo("senderId", userId1)
                .whereEqualTo("receiverId", userId2)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> {
                    Map<String, Object> messageData = doc.getData();
                    long timestamp = Long.parseLong(messageData.get("timestamp").toString());
                    Date date = new Date(timestamp);
                    return new Message(
                            (String) messageData.get("senderId"),
                            (String) messageData.get("receiverId"),
                            (String) messageData.get("message"),
                            date
                    );
                })
                .collect(Collectors.toList());

        List<Message> messagesSentByUser2 = db.collection("messages")
                .whereEqualTo("senderId", userId2)
                .whereEqualTo("receiverId", userId1)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> {
                    Map<String, Object> messageData = doc.getData();
                    long timestamp = Long.parseLong(messageData.get("timestamp").toString());
                    Date date = new Date(timestamp);
                    return new Message(
                            (String) messageData.get("senderId"),
                            (String) messageData.get("receiverId"),
                            (String) messageData.get("message"),
                            date
                    );
                })
                .collect(Collectors.toList());

        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(messagesSentByUser1);
        allMessages.addAll(messagesSentByUser2);
        allMessages.sort(Comparator.comparing(Message::getTimestamp));

        return allMessages;
    }

    public List<Message> getMessagesByUser(String userId) throws ExecutionException, InterruptedException {
        List<Message> sentMessages = db.collection("messages")
                .whereEqualTo("senderId", userId)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> {
                    Map<String, Object> messageData = doc.getData();
                    long timestamp = Long.parseLong(messageData.get("timestamp").toString());
                    Date date = new Date(timestamp);
                    return new Message(
                            (String) messageData.get("senderId"),
                            (String) messageData.get("receiverId"),
                            (String) messageData.get("message"),
                            date
                    );
                })
                .collect(Collectors.toList());

        List<Message> receivedMessages = db.collection("messages")
                .whereEqualTo("receiverId", userId)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> {
                    Map<String, Object> messageData = doc.getData();
                    long timestamp = Long.parseLong(messageData.get("timestamp").toString());
                    Date date = new Date(timestamp);
                    return new Message(
                            (String) messageData.get("senderId"),
                            (String) messageData.get("receiverId"),
                            (String) messageData.get("message"),
                            date
                    );
                })
                .collect(Collectors.toList());

        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(receivedMessages);
        allMessages.sort(Comparator.comparing(Message::getTimestamp));

        return allMessages;
    }
}
