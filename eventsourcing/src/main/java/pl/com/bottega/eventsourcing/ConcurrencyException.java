package pl.com.bottega.eventsourcing;

import java.util.UUID;

public class ConcurrencyException extends RuntimeException {
    public ConcurrencyException(UUID aggregateId, Long lastEventAggregateVersion, Long expectedVersion) {
        super(String.format(
            "Concurrent modification of aggregate id = %s, expected version = %s, actual version = %s",
            aggregateId, expectedVersion, lastEventAggregateVersion
            )
        );
    }

    public ConcurrencyException(UUID aggregateId) {
        super("Failed to create aggregate with id = {} because it already exists");
    }
}
