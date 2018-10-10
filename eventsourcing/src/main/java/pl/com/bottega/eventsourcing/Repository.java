package pl.com.bottega.eventsourcing;

import java.util.UUID;

public interface Repository<A extends AggregateRoot> {

    A get(UUID id);
    void save(A aggregateRoot, Long expectedVersion);

}
