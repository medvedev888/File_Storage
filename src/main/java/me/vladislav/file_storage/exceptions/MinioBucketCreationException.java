package me.vladislav.file_storage.exceptions;

public class MinioBucketCreationException extends RuntimeException {
    public MinioBucketCreationException(String message) {
        super(message);
    }

    public MinioBucketCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
