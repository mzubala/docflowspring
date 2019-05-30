package pl.com.bottega.docflowjee.docflow.adapters.jms;

import org.springframework.jms.core.JmsTemplate;
import pl.com.bottega.eventsourcing.Event;
import pl.com.bottega.eventsourcing.EventPublisher;

public class JMSPublisher implements EventPublisher {

    private final JmsTemplate jmsTemplate;

    public JMSPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void publish(Event event) {
        jmsTemplate.convertAndSend(topicName(event), event);
    }

    private String topicName(Event event) {
        return String.format("docflow/%s", event.getClass().getSimpleName());
    }
}
