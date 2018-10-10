package pl.com.bottega.eventsourcing;

import org.junit.Test;
import pl.com.bottega.eventsourcing.testdata.TestAggregateCreatedEvent;
import pl.com.bottega.eventsourcing.testdata.TestAggregateRoot;
import pl.com.bottega.eventsourcing.testdata.TestValueChangedEvent;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AggregateRootTest {

    private final UUID id = UUID.randomUUID();
    private Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    @Test
    public void emitsCreatedEventAfterCreation() {
        TestAggregateRoot testAggregateRoot = new TestAggregateRoot(id, clock);

        assertThat(testAggregateRoot.getUncommitedEvents())
            .contains(new TestAggregateCreatedEvent(id, clock.instant()));
    }

    @Test
    public void emitsTestValueChangedEvent() {
        TestAggregateRoot testAggregateRoot = new TestAggregateRoot(id, clock);

        testAggregateRoot.changeState("new state", clock);

        assertThat(testAggregateRoot.getUncommitedEvents())
            .contains(new TestValueChangedEvent(id, clock.instant(), "new state"));
    }

}
