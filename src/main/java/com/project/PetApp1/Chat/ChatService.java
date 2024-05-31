package com.project.PetApp1.Chat;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import com.google.cloud.firestore.WriteResult;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;





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
    /*public void listenForMessages() {
        db.collection("messages")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        System.err.println("Listen failed: " + e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Map<String, Object> messageData = dc.getDocument().getData();
                                    long timestamp = (long) messageData.get("timestamp");
                                    Date date = new Date(timestamp);
                                    Message message = new Message(
                                            (String) messageData.get("senderId"),
                                            (String) messageData.get("receiverId"),
                                            (String) messageData.get("message"),
                                            date
                                    );
                                    // Yeni mesajı işleme al
                                    System.out.println("New message: " + message);
                                    break;
                                case MODIFIED:
                                    System.out.println("Modified message: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    System.out.println("Removed message: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Current data: null");
                    }
                });
    }*/


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
}
