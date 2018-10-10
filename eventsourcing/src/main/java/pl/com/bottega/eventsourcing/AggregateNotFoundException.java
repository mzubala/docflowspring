package pl.com.bottega.eventsourcing;

import java.util.UUID;

public class AggregateNotFoundException extends RuntimeException {

    public AggregateNotFoundException(UUID aggregateId) {
        super(String.format("Aggregate with id={} not found", aggregateId.toString()));
    }

}
