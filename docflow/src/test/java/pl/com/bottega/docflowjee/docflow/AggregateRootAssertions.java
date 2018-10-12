package pl.com.bottega.docflowjee.docflow;

import org.assertj.core.api.Assertions;
import pl.com.bottega.eventsourcing.AggregateRoot;
import pl.com.bottega.eventsourcing.Event;

import java.util.List;
import java.util.stream.Collectors;

public class AggregateRootAssertions {

    private final AggregateRoot aggregateRoot;

    public AggregateRootAssertions(AggregateRoot aggregateRoot) {
        this.aggregateRoot = aggregateRoot;
    }

    public static AggregateRootAssertions assertThatAggregate(AggregateRoot aggregateRoot) {
        return new AggregateRootAssertions(aggregateRoot);
    }

    public void emitted(Event... events) {
        Assertions.assertThat(aggregateRoot.getUncommitedEvents()).contains(events);
    }

    public void emittedExactly(Event... events) {
        Assertions.assertThat(aggregateRoot.getUncommitedEvents()).containsExactly(events);
    }

    public void emittedNoEvents() {
        Assertions.assertThat(aggregateRoot.getUncommitedEvents()).isEmpty();
    }

    public <E extends Event> void didNotEmit(Class<E>... eventClasses) {
        List<Class<E>> classes = aggregateRoot.getUncommitedEvents().stream()
            .map(Object::getClass).map(it -> (Class<E>) it)
            .collect(Collectors.toList());
        Assertions.assertThat(classes).doesNotContain(eventClasses);
    }


}
