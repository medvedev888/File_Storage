package me.vladislav.file_storage.exceptions.folder;

public class RetrievingFoldersException extends RuntimeException {
    public RetrievingFoldersException(String message) {
        super(message);
    }

    public RetrievingFoldersException(String message, Throwable cause) {
        super(message, cause);
    }
}
