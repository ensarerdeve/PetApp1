package com.project.PetApp1.Chat;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        String firebasePath = System.getenv("FIREBASE");
        FileInputStream serviceAccount = new FileInputStream(firebasePath);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://chat-service1.firebaseio.com")
                .build();

        return FirebaseApp.initializeApp(options);
    }
}