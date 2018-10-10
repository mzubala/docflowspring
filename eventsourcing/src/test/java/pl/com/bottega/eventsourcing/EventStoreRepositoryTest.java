package pl.com.bottega.eventsourcing;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import pl.com.bottega.eventsourcing.testdata.TestAggregateRoot;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EventStoreRepositoryTest {

    private final EventPublisher eventPublisher = mock(EventPublisher.class);
    private final EventStore eventStore = new InMemoryEventStore(eventPublisher);
    private final EventStoreRepository<TestAggregateRoot> sut = new EventStoreRepository<>(eventStore, TestAggregateRoot.class);
    private Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    private final UUID id = UUID.randomUUID();

    @Test
    public void savesAggregatesInRepository() {
        TestAggregateRoot agg = new TestAggregateRoot(id, clock);

        sut.save(agg, -1L);
        TestAggregateRoot fetched = sut.get(id);

        assertThat(fetched).isNotNull();
        assertThat(fetched.getUncommitedEvents()).isEmpty();
        assertThat(fetched.getId()).isEqualTo(id);
    }

    @Test
    public void savesStateChanges() {
        TestAggregateRoot agg = new TestAggregateRoot(id, clock);
        sut.save(agg, -1L);

        TestAggregateRoot fetched = sut.get(id);
        fetched.changeState("new state", clock);
        sut.save(fetched,  0L);
        fetched = sut.get(id);

        assertThat(fetched).isNotNull();
        assertThat(fetched.getState()).isEqualTo("new state");
    }

    @Test
    public void clearsUncommitedEventsOnSave() {
        TestAggregateRoot agg = new TestAggregateRoot(id, clock);
        agg.changeState("new state", clock);

        sut.save(agg, -1L);

        assertThat(agg.getUncommitedEvents().size()).isEqualTo(0);
    }

    @Test
    public void uncommitedEventsAreEmptyAfterLoad() {
        TestAggregateRoot agg = new TestAggregateRoot(id, clock);
        agg.changeState("new state", clock);
        sut.save(agg, -1L);

        TestAggregateRoot fetched = sut.get(id);

        assertThat(fetched.getUncommitedEvents().size()).isEqualTo(0);
    }

    @Test
    public void eventsArePublishedOnSave() {
        TestAggregateRoot agg = new TestAggregateRoot(id, clock);
        agg.changeState("new state", clock);

        sut.save(agg, -1L);

        verify(eventPublisher, times(2)).publish(any(Event.class));
    }

    @Test
    public void notifiesPostLoadCallbacl() {
        TestAggregateRoot agg = new TestAggregateRoot(id, clock);
        sut.save(agg, -1L);
        Consumer<TestAggregateRoot> callback = mock(Consumer.class);
        sut.setPostLoadCallback(callback);

        sut.get(id);

        ArgumentCaptor<TestAggregateRoot> captor = ArgumentCaptor.forClass(TestAggregateRoot.class);
        verify(callback).accept(captor.capture());
        assertThat(captor.getValue().id).isEqualTo(id);
    }

}
