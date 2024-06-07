package me.vladislav.file_storage.exceptions.folders;

public class RetrievingFoldersException extends RuntimeException {
    public RetrievingFoldersException(String message) {
        super(message);
    }

    public RetrievingFoldersException(String message, Throwable cause) {
        super(message, cause);
    }
}
