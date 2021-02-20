package tech.becoming.frameworks.filestore.dropwizard.notification;

// import javax.ejb.Stateless;
// import javax.jms.JMSContext;
// import javax.jms.Message;
// import javax.jms.Topic;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationServiceBean implements NotificationService {

    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());

    // @Resource(name = "java:/jms/topic/notification")
    // private Topic notification;

    // @Inject
    // private JMSContext context;

    @Override
    public void throwEvent(String type, String sourceId) throws NotificationServiceException {
        LOGGER.log(Level.FINE, "Throwing event of type: " + type);
        // try {
        //     Message message = context.createMessage();
        //     message.setStringProperty(Event.ID, UUID.randomUUID().toString());
        //     message.setStringProperty(Event.TIMESTAMP, "" + System.currentTimeMillis());
        //     message.setStringProperty(Event.EVENT_TYPE, type);
        //     message.setStringProperty(Event.SOURCE_ID, sourceId);
        //     context.createProducer().send(notification, message);
        // } catch (Exception e) {
        //     throw new NotificationServiceException("unable to throw event", e);
        // }
    }
}
