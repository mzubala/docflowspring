package pl.com.bottega.eventsourcing;

import io.vavr.control.Option;
import io.vavr.control.Try;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class EventStoreRepository<A extends AggregateRoot> implements Repository<A> {

    private final EventStore eventStore;
    private final Class<A> klass;
    private Option<Consumer<A>> postLoadCallback = Option.none();

    public EventStoreRepository(EventStore eventStore, Class<A> klass) {
        this.eventStore = eventStore;
        this.klass = klass;
    }

    @Override
    public A get(UUID id) {
        List<Event> events = eventStore.getEventsForAggregate(id);
        if (events.size() == 0) {
            throw new AggregateNotFoundException(id);
        }
        A aggregateRoot = instantiateAggregate();
        aggregateRoot.loadFromHistory(events);
        postLoadCallback.peek(it -> it.accept(aggregateRoot));
        return aggregateRoot;
    }

    private A instantiateAggregate() {
        return Try.of(() -> {
            Constructor<A> constructor = klass.getDeclaredConstructor();
            constructor.setAccessible(true);
            A agg = constructor.newInstance();
            constructor.setAccessible(false);
            return agg;
        }).getOrElseThrow((e) -> new RuntimeException("Failed to instantiate aggregate", e));
    }

    @Override
    public void save(AggregateRoot aggregateRoot, Long expectedVersion) {
        if(aggregateRoot.getUncommitedEvents().isEmpty()) {
            return;
        }
        eventStore.saveEvents(aggregateRoot.getId(), aggregateRoot.getUncommitedEvents(), expectedVersion);
        aggregateRoot.markChangesCommited();
    }

    public void setPostLoadCallback(Consumer<A> postLoadCallback) {
        this.postLoadCallback = Option.of(postLoadCallback);
    }
}
