package com.technews.aggregate.springframework.domain.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.technews.aggregate.springframework.domain.Release;
import com.technews.aggregate.springframework.dto.SaveReleaseRequest;
import com.technews.common.constant.Collections;
import com.technews.common.exception.FirebaseQuerySnapshotException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ReleasesRepository {

    private static final Firestore FIRE_STORE = FirestoreClient.getFirestore();
    private static final String COLLECTION_NAME = Collections.RELEASES.key();

    public void insertRelease(SaveReleaseRequest saveReleaseRequest) {
        Query query = FIRE_STORE.collection(COLLECTION_NAME)
                .whereEqualTo("project", saveReleaseRequest.getProject())
                .whereEqualTo("version", saveReleaseRequest.getVersion());

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (isNotExistDocument(querySnapshot)) {
            DocumentReference document = FIRE_STORE.collection(COLLECTION_NAME).document();
            final Release release = saveReleaseRequest.toRelease(document.getId());
            document.set(release);
            log.info("new release document added. document ID: {}", document.getId());
            return;
        }

        log.warn("this release information already exists.");
    }

    public List<Release> findAllReleases() {
        try {
            ApiFuture<QuerySnapshot> future = FIRE_STORE.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<Release> result = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                result.add(document.toObject(Release.class));
            }
            return result;
        } catch (Exception e) {
            log.error("querySnapshot exception. {}", e.getMessage(), e);
            throw new FirebaseQuerySnapshotException();
        }
    }

    private boolean isNotExistDocument(ApiFuture<QuerySnapshot> querySnapshot) {
        try {
            return querySnapshot.get().isEmpty();
        } catch (Exception e) {
            log.error("querySnapshot exception. {}", e.getMessage(), e);
            throw new FirebaseQuerySnapshotException();
        }
    }

    private boolean isExistDocument(ApiFuture<QuerySnapshot> querySnapshot) {
        return !this.isNotExistDocument(querySnapshot);
    }
}
