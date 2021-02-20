package fr.miage.filestore.dropwizard.notification;

// import javax.ejb.ActivationConfigProperty;
// import javax.ejb.MessageDriven;
// import javax.jms.JMSException;
// import javax.jms.Message;
// import javax.jms.MessageListener;

import java.util.logging.Logger;

// @MessageDriven(name = "MetaDataExtractorListener", activationConfig = {
//         @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/topic/notification"),
//         @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
// })
public class MetaDataExtractorEventListener { //implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(MetaDataExtractorEventListener.class.getName());

    // @Override
    // public void onMessage(Message message) {
    //     try {
    //         Event event = new Event();
    //         event.fromJMSMessage(message);
    //         LOGGER.log(Level.FINE, "Event received: " + event.toString());
    //         //TODO Add the extraction code
    //     } catch (JMSException e) {
    //         LOGGER.log(Level.SEVERE, "error while receiving event", e);
    //     }
    // }

}