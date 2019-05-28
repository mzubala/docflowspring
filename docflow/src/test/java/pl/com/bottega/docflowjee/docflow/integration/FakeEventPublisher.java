package pl.com.bottega.docflowjee.docflow.integration;

import pl.com.bottega.eventsourcing.Event;
import pl.com.bottega.eventsourcing.EventPublisher;

import java.util.LinkedList;
import java.util.List;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class FakeEventPublisher implements EventPublisher {

    private List<Event> publishedEvents = new LinkedList<>();

    @Override
    public void publish(Event event) {
        publishedEvents.add(event);
    }

    public void assertEventsWerePublishedInOrder(Class<? extends Event>... types) {
        List publishedClasses = publishedEvents.stream().map(Object::getClass).collect(toList());
        assertThat(publishedClasses).containsExactly(types);
    }
}
