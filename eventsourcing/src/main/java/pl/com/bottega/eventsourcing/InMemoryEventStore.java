package pl.com.bottega.eventsourcing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEventStore implements EventStore {

    private final EventPublisher eventPublisher;

    private final Map<UUID, List<Event>> storedEvents = new ConcurrentHashMap<>();

    public InMemoryEventStore(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void saveEvents(UUID aggregateId, List<Event> events, Long expectedVersion) {
        if(storedEvents.containsKey(aggregateId)) {
            addEventsForExistingAggregate(aggregateId, events, expectedVersion);
        }
        else {
            createListForNewAggergate(aggregateId, events, expectedVersion);
        }
        setVersions(events, expectedVersion);
        publishEvents(events);
    }

    private void addEventsForExistingAggregate(UUID aggregateId, List<Event> events, Long expectedVersion) {
        List<Event> currentEvents = storedEvents.get(aggregateId);
        checkOptimisticLocking(aggregateId, expectedVersion, currentEvents);
        currentEvents.addAll(events);
    }

    private void setVersions(List<Event> events, Long expectedVersion) {
        for(Event event : events) {
            event.setAggregateVersion(++expectedVersion);
        }
    }

    private void checkOptimisticLocking(UUID aggregateId, Long expectedVersion, List<Event> currentEvents) {
        Event lastEvent = currentEvents.get(currentEvents.size() - 1);
        Long lastEventAggregateVersion = lastEvent.getAggregateVersion();
        if(!lastEventAggregateVersion.equals(expectedVersion)) {
            throw new ConcurrencyException(aggregateId, lastEventAggregateVersion, expectedVersion);
        }
    }

    private void createListForNewAggergate(UUID aggregateId, List<Event> events, Long expectedVersion) {
        if(expectedVersion.equals(-1)) {
            throw new ConcurrencyException(aggregateId);
        }
        storedEvents.put(aggregateId, new ArrayList<>(events));
    }

    private void publishEvents(List<Event> events) {
        events.forEach(eventPublisher::publish);
    }

    @Override
    public List<Event> getEventsForAggregate(UUID aggregateId) {
        return storedEvents.getOrDefault(aggregateId, Collections.emptyList());
    }
}
