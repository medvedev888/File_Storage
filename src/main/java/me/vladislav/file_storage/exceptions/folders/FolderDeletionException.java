package me.vladislav.file_storage.exceptions.folders;

public class FolderDeletionException extends RuntimeException {

    public FolderDeletionException(String message) {
        super(message);
    }

    public FolderDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

}
