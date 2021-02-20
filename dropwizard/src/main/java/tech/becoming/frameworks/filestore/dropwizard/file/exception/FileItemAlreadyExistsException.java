package tech.becoming.frameworks.filestore.dropwizard.file.exception;

public class FileItemAlreadyExistsException extends Exception {

    public FileItemAlreadyExistsException(String message) {
        super(message);
    }
}
