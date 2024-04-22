package com.technews.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.FileInputStream;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Profile("!prod")
    @Configuration
    static class DevConfig {
        @PostConstruct
        public void devInitialization() {
            try {
                FileInputStream serviceAccount = new FileInputStream("src/main/resources/static/accountKey/firebaseAccountKey-test.json");
                FirebaseOptions.Builder optionBuilder = FirebaseOptions.builder();
                FirebaseOptions options = optionBuilder
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("FirebaseApp initialize exception. {}", e.getMessage(), e);
            }
        }
    }

    @Profile("prod")
    @Configuration
    static class ProdConfig {
        @PostConstruct
        public void prodInitialization() {
            try {
                FileInputStream serviceAccount = new FileInputStream("src/main/resources/static/accountKey/firebaseAccountKey.json");
                FirebaseOptions.Builder optionBuilder = FirebaseOptions.builder();
                FirebaseOptions options = optionBuilder
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("FirebaseApp initialize exception. {}", e.getMessage(), e);
            }
        }
    }
}