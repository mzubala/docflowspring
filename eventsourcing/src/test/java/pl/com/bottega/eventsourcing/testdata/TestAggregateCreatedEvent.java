package pl.com.bottega.eventsourcing.testdata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.UUID;

public class TestAggregateCreatedEvent extends Event {

    @JsonCreator
    public TestAggregateCreatedEvent(@JsonProperty("aggregateId") UUID aggregateId,
                                     @JsonProperty("createdAt") Instant createdAt) {
        super(aggregateId, createdAt);
    }

}
