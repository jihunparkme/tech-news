package com.technews.aggregate.releases.springframework.domain.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.technews.aggregate.releases.springframework.domain.Release;
import com.technews.aggregate.releases.springframework.dto.SaveReleaseRequest;
import com.technews.common.constant.Collections;
import com.technews.common.exception.FirebaseQuerySnapshotException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.google.cloud.firestore.Query.Direction.DESCENDING;

@Slf4j
@Repository
public class ReleasesRepository {

    private static final String COLLECTION_NAME = Collections.RELEASES.key();

    public void save(SaveReleaseRequest saveReleaseRequest) {
        Query query = FirestoreClient.getFirestore().collection(COLLECTION_NAME)
                .whereEqualTo("project", saveReleaseRequest.getProject())
                .whereEqualTo("version", saveReleaseRequest.getVersion());

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (isNotExistDocument(querySnapshot)) {
            DocumentReference document = FirestoreClient.getFirestore().collection(COLLECTION_NAME).document();
            final Release release = saveReleaseRequest.toRelease(document.getId());
            document.set(release);
            log.info("new release document added. document ID: {}", document.getId());
            return;
        }

        log.warn("this release information already exists.");
    }

    public List<Release> findAll() {
        try {
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<Release> result = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                result.add(document.toObject(Release.class));
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error("querySnapshot exception. {}", e.getMessage(), e);
            throw new FirebaseQuerySnapshotException();
        }
    }

    public List<Release> findLatestReleaseDate(final String project) {
        try {
            ApiFuture<QuerySnapshot> future = FirestoreClient.getFirestore().collection(COLLECTION_NAME)
                    .whereEqualTo("project", project)
                    .orderBy("version", DESCENDING)
                    .limit(1)
                    .get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<Release> result = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                result.add(document.toObject(Release.class));
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error("querySnapshot exception. {}", e.getMessage(), e);
            throw new FirebaseQuerySnapshotException();
        }
    }

    private boolean isNotExistDocument(ApiFuture<QuerySnapshot> querySnapshot) {
        try {
            return querySnapshot.get().isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            log.error("querySnapshot exception. {}", e.getMessage(), e);
            throw new FirebaseQuerySnapshotException();
        }
    }

    private boolean isExistDocument(ApiFuture<QuerySnapshot> querySnapshot) {
        return !this.isNotExistDocument(querySnapshot);
    }
}
