package fr.miage.filestore.dropwizard.notification;

// import javax.jms.JMSException;
// import javax.jms.Message;

public class Event {

    public static final String ID = "id";
    public static final String TIMESTAMP = "timestamp";
    public static final String EVENT_TYPE = "eventType";
    public static final String SOURCE_ID = "sourceId";

    private String id;
    private long timestamp;
    private String eventType;
    private String sourceId;

    public Event() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    // public void fromJMSMessage(Message message) throws JMSException {
    //     this.setId(message.getStringProperty(Event.ID));
    //     this.setTimestamp(Long.parseLong(message.getStringProperty(Event.TIMESTAMP)));
    //     this.setEventType(message.getStringProperty(Event.EVENT_TYPE));
    //     this.setSourceId(message.getStringProperty(Event.SOURCE_ID));
    // }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", eventType='" + eventType + '\'' +
                ", sourceId='" + sourceId + '\'' +
                '}';
    }
}
