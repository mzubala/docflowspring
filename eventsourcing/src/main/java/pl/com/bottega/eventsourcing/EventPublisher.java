package pl.com.bottega.eventsourcing;

public interface EventPublisher {

    void publish(Event event);

}
