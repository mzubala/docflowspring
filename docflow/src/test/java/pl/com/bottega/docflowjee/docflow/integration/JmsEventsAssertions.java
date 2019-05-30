package pl.com.bottega.docflowjee.docflow.integration;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import pl.com.bottega.eventsourcing.Event;

@Component
public class JmsEventsAssertions {

    private final JmsTemplate jmsTemplate;

    public JmsEventsAssertions(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void assertEventsWerePublishedInOrder(Class<? extends Event>... types) {
    }

    public void assertEventWasPublished(Class<? extends Event> eventClass) {
    }

    public void receive(Event event) {
    }

    public void reset() {

    }

}
