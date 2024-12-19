package com.technews.common.exception;

public class FirebaseQuerySnapshotException extends RuntimeException {
    public FirebaseQuerySnapshotException() {
        super("Query Snapshot Exception.");
    }

    public FirebaseQuerySnapshotException(String message, Throwable cause) {
        super(message, cause);
    }

    public FirebaseQuerySnapshotException(String message) {
        super(message);
    }

    public FirebaseQuerySnapshotException(Throwable cause) {
        super(cause);
    }
}
