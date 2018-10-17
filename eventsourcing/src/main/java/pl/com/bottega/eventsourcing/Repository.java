package pl.com.bottega.eventsourcing;

import java.util.Optional;
import java.util.UUID;

public interface Repository<A extends AggregateRoot> {

    A get(UUID id) throws AggregateNotFoundException;

    Optional<A> getOptionally(UUID id);

    void save(A aggregateRoot, Long expectedVersion);

}
