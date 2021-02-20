package tech.becoming.frameworks.filestore.dropwizard.notification;

public interface NotificationService {

    void throwEvent(String type, String sourceId) throws NotificationServiceException;

}
