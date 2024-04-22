package com.technews.firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.technews.springProjects.dto.Release;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Disabled
public class CloudFirestoreTest {

    public static final String COLLECTION_NAME = "RELEASES";
    public static final String PROJECT = "spring-boot";
    public static final String VERSION = "Release v3.3.0-RC1";

    @BeforeEach
    void BeforeEach() {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/static/accountKey/firebaseAccountKey-test.json");
            FirebaseOptions.Builder optionBuilder = FirebaseOptions.builder();
            FirebaseOptions options = optionBuilder
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void add() {
        Query query = FirestoreClient.getFirestore().collection(COLLECTION_NAME)
                .whereEqualTo("project", PROJECT)
                .whereEqualTo("version", VERSION);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (isNotExistDocument(querySnapshot)) {
            DocumentReference document = FirestoreClient.getFirestore().collection(COLLECTION_NAME).document();
            final Release release = Release.builder()
                    .id(document.getId())
                    .project(PROJECT)
                    .version(VERSION)
                    .date("Apr 18, 2024")
                    .url("https://github.com/spring-projects/spring-boot/releases/tag/v3.3.0-RC1")
                    .build();
            document.set(release);
            log.info("new document added. document ID: {}", document.getId());
        } else {
            log.warn("this release information already exists.");
        }
    }

    @Test
    void update() throws Exception {
        Query query = FirestoreClient.getFirestore().collection(COLLECTION_NAME)
                .whereEqualTo("project", PROJECT)
                .whereEqualTo("version", VERSION);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (isExistDocument(querySnapshot)) {
            Query findQuery = FirestoreClient.getFirestore().collection(COLLECTION_NAME)
                    .whereEqualTo("project", PROJECT)
                    .whereEqualTo("version", VERSION);
            ApiFuture<QuerySnapshot> future = findQuery.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            final Release result = documents.get(0).toObject(Release.class);

            DocumentReference document = FirestoreClient.getFirestore().collection(COLLECTION_NAME).document(result.getId());
            document.update("date", "Apr 21, 2024");
            log.info("the document has been updated. document ID: {}", document.getId());
        } else {
            log.warn("this release information already exists.");
        }
    }

    @Test
    void find_all() throws Exception {
        ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Release> result = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            result.add(document.toObject(Release.class));
        }

        assertEquals(1, result.size());
        assertEquals("spring-boot", result.get(0).getProject());
        assertEquals("Release v3.3.0-RC1", result.get(0).getVersion());
        assertEquals("Apr 21, 2024", result.get(0).getDate());
        assertEquals("https://github.com/spring-projects/spring-boot/releases/tag/v3.3.0-RC1", result.get(0).getUrl());
    }

    @Test
    void find() throws Exception {
        Query query = FirestoreClient.getFirestore().collection(COLLECTION_NAME)
                .whereEqualTo("project", PROJECT)
                .whereEqualTo("version", VERSION);

        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        final Release result = documents.get(0).toObject(Release.class);

        assertEquals("spring-boot", result.getProject());
        assertEquals("Release v3.3.0-RC1", result.getVersion());
        assertEquals("Apr 21, 2024", result.getDate());
        assertEquals("https://github.com/spring-projects/spring-boot/releases/tag/v3.3.0-RC1", result.getUrl());
    }

    @Test
    void delete() throws Exception {
        Query query = FirestoreClient.getFirestore().collection(COLLECTION_NAME)
                .whereEqualTo("project", PROJECT)
                .whereEqualTo("version", VERSION);
        ApiFuture<QuerySnapshot> future = query.get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (!documents.isEmpty()) {
            Release user = documents.get(0).toObject(Release.class);
            FirestoreClient.getFirestore().collection(COLLECTION_NAME).document(user.getId()).delete();
        }
    }

    private boolean isExistDocument(ApiFuture<QuerySnapshot> querySnapshot) {
        return !this.isNotExistDocument(querySnapshot);
    }

    private boolean isNotExistDocument(ApiFuture<QuerySnapshot> querySnapshot) {
        try {
            return querySnapshot.get().isEmpty();
        } catch (Exception e) {
            throw new RuntimeException("document search failed.");
        }
    }
}
