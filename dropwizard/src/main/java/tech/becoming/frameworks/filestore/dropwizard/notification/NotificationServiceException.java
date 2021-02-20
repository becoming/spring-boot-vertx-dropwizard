package tech.becoming.frameworks.filestore.dropwizard.notification;

public class NotificationServiceException extends Exception {

    public NotificationServiceException(String message) {
        super(message);
    }

    public NotificationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
