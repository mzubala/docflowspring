package pl.com.bottega.eventsourcing;

import java.util.UUID;

public class ConcurrencyException extends RuntimeException {
    public ConcurrencyException(UUID aggregateId, Long lastEventAggregateVersion, Long expectedVersion) {
        super(String.format(
            "Concurrent modification of aggregate id = {}, expected version = {}, actual version = {}",
            aggregateId, expectedVersion, lastEventAggregateVersion
            )
        );
    }

    public ConcurrencyException(UUID aggregateId) {
        super("Failed to create aggregate with id = {} because it already exists");
    }
}
