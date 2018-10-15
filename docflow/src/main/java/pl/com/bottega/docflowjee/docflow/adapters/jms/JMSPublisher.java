package pl.com.bottega.docflowjee.docflow.adapters.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import pl.com.bottega.eventsourcing.Event;
import pl.com.bottega.eventsourcing.EventPublisher;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class JMSPublisher implements EventPublisher {

    @Inject
    private JMSContext jmsContext;
    @Inject
    private ObjectMapper mapper;

    @Override
    public void publish(Event event) {
        Topic topic = jmsContext.createTopic(event.getClass().getName());
        TextMessage message = jmsContext.createTextMessage();
        try {
            message.setText(eventJson(event));
        } catch (Exception e) {
            new RuntimeException("Failed to create message", e);
        }
        jmsContext.createProducer().send(topic, message);
    }

    private String eventJson(Event event) {
        return Try.of(() -> mapper.writeValueAsString(event))
            .getOrElseThrow((e) -> new RuntimeException("Failed to serialize event", e));
    }
}
