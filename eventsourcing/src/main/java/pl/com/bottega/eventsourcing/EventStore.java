package pl.com.bottega.eventsourcing;

import java.util.List;
import java.util.UUID;

public interface EventStore {

    void saveEvents(UUID aggregateId, List<Event> events, Long expectedVersion);

    List<Event> getEventsForAggregate(UUID aggregateId);

}
