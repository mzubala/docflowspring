package pl.com.bottega.docflowjee.eventsourcing.mongo;

import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.UUID;

public class TestAggregateCreatedEvent extends Event {

    public TestAggregateCreatedEvent(UUID aggregateId, Instant createdAt) {
        super(aggregateId, createdAt);
    }

    @Override
    public String toString() {
        return "TestAggregateCreatedEvent{} " + super.toString();
    }
}
