package pl.com.bottega.docflowjee.eventsourcing.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.bottega.eventsourcing.ConcurrencyException;
import pl.com.bottega.eventsourcing.Event;
import pl.com.bottega.eventsourcing.EventStore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
public class MongoEventStore implements EventStore {

    @Autowired
    private PersistentEventRepository repository;


    @Override
    public void saveEvents(UUID aggregateId, List<Event> events, Long expectedVersion) {
        checkOptimisticLocking(aggregateId, expectedVersion);
        setVersions(events, expectedVersion);
        List<PersistentEvent> toSave = createPersistentEvents(aggregateId, events, expectedVersion);
        repository.saveAll(toSave);
    }

    private List<PersistentEvent> createPersistentEvents(UUID aggregateId, List<Event> events, Long expectedVersion) {
        return events.stream().map((e) -> {
            PersistentEvent persistentEvent = new PersistentEvent();
            persistentEvent.aggregateId = aggregateId;
            persistentEvent.payload = e;
            persistentEvent.timestamp = e.getCreatedAt();
            persistentEvent.version = e.getAggregateVersion();
            return persistentEvent;
        }).collect(toList());
    }

    private void checkOptimisticLocking(UUID aggregateId, Long expectedVersion) {
        Optional<PersistentEvent> lastEvent = repository.findFirstByAggregateIdOrderByTimestampDesc(aggregateId);
        if(lastEvent.isEmpty() && expectedVersion.equals(-1L)) {
            return;
        }
        Long lastVersion = lastEvent.get().version;
        if(!lastVersion.equals(expectedVersion)) {
            throw new ConcurrencyException(aggregateId, lastVersion, expectedVersion);
        }
    }

    private void setVersions(List<Event> events, Long expectedVersion) {
        for(Event event : events) {
            event.setAggregateVersion(++expectedVersion);
        }
    }

    @Override
    public List<Event> getEventsForAggregate(UUID aggregateId) {
        return repository.findByAggregateId(aggregateId).stream().map(pe -> pe.payload).collect(toList());
    }
}
