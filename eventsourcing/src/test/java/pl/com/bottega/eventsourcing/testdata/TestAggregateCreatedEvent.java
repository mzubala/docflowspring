package pl.com.bottega.eventsourcing.testdata;

import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.UUID;

public class TestAggregateCreatedEvent extends Event {

    public TestAggregateCreatedEvent(UUID aggregateId, Instant createdAt) {
        super(aggregateId, createdAt);
    }

}
