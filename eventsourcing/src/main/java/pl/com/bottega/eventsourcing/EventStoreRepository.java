package pl.com.bottega.eventsourcing;

import io.vavr.control.Try;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class EventStoreRepository<A extends AggregateRoot> implements Repository<A> {

    private final EventStore eventStore;
    private final Class<A> klass;
    private Optional<Consumer<A>> postLoadCallback = Optional.empty();

    public EventStoreRepository(EventStore eventStore, Class<A> klass) {
        this.eventStore = eventStore;
        this.klass = klass;
    }

    @Override
    public A get(UUID id) {
        return getOptionally(id).orElseThrow(() -> new AggregateNotFoundException(id));
    }

    @Override
    public Optional<A> getOptionally(UUID id) {
        List<Event> events = eventStore.getEventsForAggregate(id);
        if (events.size() == 0) {
            return Optional.empty();
        }
        A aggregateRoot = instantiateAggregate();
        aggregateRoot.loadFromHistory(events);
        postLoadCallback.ifPresent(it -> it.accept(aggregateRoot));
        return Optional.of(aggregateRoot);
    }

    private A instantiateAggregate() {
        return Try.of(() -> {
            Constructor<A> constructor = klass.getDeclaredConstructor();
            constructor.setAccessible(true);
            A agg = constructor.newInstance();
            constructor.setAccessible(false);
            return agg;
        })
            .getOrElseThrow((e) -> new RuntimeException("Failed to instantiate aggregate", e));
    }

    @Override
    public void save(AggregateRoot aggregateRoot, Long expectedVersion) {
        eventStore.saveEvents(aggregateRoot.getId(), aggregateRoot.getUncommitedEvents(), expectedVersion);
        aggregateRoot.markChangesCommited();
    }

    public void setPostLoadCallback(Consumer<A> postLoadCallback) {
        this.postLoadCallback = Optional.ofNullable(postLoadCallback);
    }
}
