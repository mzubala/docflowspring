package pl.com.bottega.docflowjee.eventsourcing.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import pl.com.bottega.eventsourcing.ConcurrencyException;
import pl.com.bottega.eventsourcing.Event;
import pl.com.bottega.eventsourcing.EventPublisher;
import pl.com.bottega.eventsourcing.EventStore;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JPAEventStore implements EventStore {

    private final ObjectMapper mapper;
    private final EventPublisher publisher;
    private final EntityManager entityManager;

    public JPAEventStore(ObjectMapper mapper, EventPublisher publisher, EntityManager entityManager) {
        this.mapper = mapper;
        this.publisher = publisher;
        this.entityManager = entityManager;
    }

    @Override
    public void saveEvents(UUID aggregateId, List<Event> events, Long expectedVersion) {
        checkOptimisticLocking(aggregateId, expectedVersion);
        setVersions(events, expectedVersion);
        events.stream()
            .map(this::toPersistentEvent)
            .forEach(this::persist);
    }

    private void checkOptimisticLocking(UUID aggregateId, Long expectedVersion) {
        Long lastVersion = (Long) entityManager.createNamedQuery("PersistentEvent.lastVersion")
            .setParameter("id", aggregateId).getSingleResult();
        if(lastVersion == null && expectedVersion.equals(-1L)) {
            return;
        }
        if(!lastVersion.equals(expectedVersion)) {
            throw new ConcurrencyException(aggregateId, lastVersion, expectedVersion);
        }
    }

    private void persist(PersistentEvent persistentEvent) {
        entityManager.persist(persistentEvent);
    }

    private PersistentEvent toPersistentEvent(Event event) {
        PersistentEvent persistentEvent = new PersistentEvent();
        persistentEvent.aggregateId = event.getAggregateId();
        persistentEvent.eventClass = event.getClass().getName();
        persistentEvent.payload = Try.of(() -> mapper.writeValueAsString(event))
            .getOrElseThrow((e) -> serializationError(event, e));
        persistentEvent.timestamp = event.getCreatedAt();
        persistentEvent.version = event.getAggregateVersion();
        return persistentEvent;
    }

    private void setVersions(List<Event> events, Long expectedVersion) {
        for(Event event : events) {
            event.setAggregateVersion(++expectedVersion);
        }
    }

    private RuntimeException serializationError(Event event, Throwable e) {
        return new RuntimeException(String.format("Failed to serialize {}", event), e);
    }

    @Override
    public List<Event> getEventsForAggregate(UUID aggregateId) {
        List<PersistentEvent> persistentEvents = entityManager
            .createNamedQuery("PersistentEvent.forAggregate")
            .setParameter("id", aggregateId).getResultList();
        return persistentEvents.stream().map(this::toEvent).collect(Collectors.toList());
    }

    private Event toEvent(PersistentEvent persistentEvent) {
        return Try.of(() -> (Event) mapper
            .readValue(persistentEvent.payload, Class.forName(persistentEvent.eventClass)))
            .getOrElseThrow((e) -> deserializationError(persistentEvent, e));
    }

    private RuntimeException deserializationError(PersistentEvent persistentEvent, Throwable cause) {
        return new RuntimeException(String.format("Failed deserialize event %s", persistentEvent.aggregateId), cause);
    }
}
