package pl.com.bottega.docflowjee.docflow.integration;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListeners;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import pl.com.bottega.eventsourcing.Event;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Component
public class JmsEventsAssertions {

    private final JmsTemplate jmsTemplate;

    private List<Event> publishedEvents = new LinkedList<>();

    public JmsEventsAssertions(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void assertEventsWerePublishedInOrder(Class<? extends Event>... types) {
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertThat(publishedClasses()).containsExactly(types));
    }

    private synchronized List publishedClasses() {
        return publishedEvents.stream().map(Object::getClass).collect(toList());
    }

    public void assertEventWasPublished(Class<? extends Event> eventClass) {
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertThat(publishedClasses()).contains(eventClass));
    }

    @JmsListener(destination = "docflow/DocumentCreatedEvent")
    @JmsListener(destination = "docflow/DocumentUpdatedEvent")
    @JmsListener(destination = "docflow/DocumentPassedToVerification")
    @JmsListener(destination = "docflow/DocumentVerifiedEvent")
    @JmsListener(destination = "docflow/DocumentRejectedEvent")
    @JmsListener(destination = "docflow/DocumentPublishedEvent")
    @JmsListener(destination = "docflow/DocumentArchivedEvent")
    @JmsListener(destination = "docflow/NewDocumentVersionCreatedEvent")
    public synchronized void receive(Event event) {
        publishedEvents.add(event);
    }

    public synchronized void reset() {
        publishedEvents.clear();
    }

}
