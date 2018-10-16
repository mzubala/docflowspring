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

@JMSDestinationDefinitions(
    value = {
        @JMSDestinationDefinition(
            name = "java:/topic/DocflowEvents",
            interfaceName = "javax.jms.Topic",
            destinationName = "DocflowEvents"
        )
    }
)
public class JMSPublisher implements EventPublisher {

    private static final String EVENT_CLASS = "EVENT_CLASS";
    @Inject
    private JMSContext jmsContext;
    @Inject
    private ObjectMapper mapper;

    @Resource(lookup = "java:/topic/DocflowEvents")
    private Topic topic;

    @Override
    public void publish(Event event) {

    }
}
