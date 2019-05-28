package pl.com.bottega.docflowjee.eventsourcing.mongo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.eventsourcing.ConcurrencyException;
import pl.com.bottega.eventsourcing.Event;
import pl.com.bottega.eventsourcing.EventPublisher;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = TestConfig.class)
@EnableMongoRepositories
@RunWith(SpringRunner.class)
public class MongoEventStoreTest {

    @Autowired
    private MongoEventStore sut;

    @Autowired
    private EventPublisher eventPublisher;

    private final UUID id = UUID.randomUUID();
    private final UUID otherId = UUID.randomUUID();
    private final TestAggregateCreatedEvent event = new TestAggregateCreatedEvent(id, Instant.parse("2019-05-25T11:29:30.000Z"));
    private final TestValueChangedEvent event2 = new TestValueChangedEvent(id, Instant.parse("2019-05-25T11:39:30.000Z"), "val2");
    private final TestValueChangedEvent event3 = new TestValueChangedEvent(id, Instant.parse("2019-05-25T11:40:30.000Z"), "val3");
    private final TestValueChangedEvent otherAggEvent = new TestValueChangedEvent(otherId, Instant.parse("2019-05-25T11:41:30.000Z"), "val");

    @Before
    public void before() {

    }

    @Test
    public void storesSingleEvent() {
        sut.saveEvents(id, Arrays.asList(event), -1L);

        List<Event> fetched = sut.getEventsForAggregate(id);

        assertThat(fetched).containsExactly(event);
    }

    @Test
    public void storesMultipleEvents() {
        sut.saveEvents(id, Arrays.asList(event, event2, event3), -1L);

        List<Event> fetched = sut.getEventsForAggregate(id);

        assertThat(fetched).containsExactly(event, event2, event3);
    }

    @Test
    public void storesEventsForMultipleAggregates() {
        sut.saveEvents(id, Arrays.asList(event, event2), -1L);
        sut.saveEvents(otherId, Arrays.asList(otherAggEvent), -1L);

        List<Event> fetched = sut.getEventsForAggregate(id);
        List<Event> fetchedOtherAgg = sut.getEventsForAggregate(otherId);

        assertThat(fetched).containsExactly(event, event2);
        assertThat(fetchedOtherAgg).containsExactly(otherAggEvent);
    }

    @Test
    public void storesEventVersion() {
        sut.saveEvents(id, Arrays.asList(event, event2), -1L);
        sut.saveEvents(id, Arrays.asList(event3), 1L);

        List<Event> fetched = sut.getEventsForAggregate(id);

        assertThat(fetched.stream().map(e -> e.getAggregateVersion()).collect(Collectors.toList()))
            .contains(0L, 1L, 2L);
    }

    @Test
    public void providesOptimisticLocking() {
        sut.saveEvents(id, Arrays.asList(event, event2), -1L);

        assertThatThrownBy(() -> sut.saveEvents(id, Arrays.asList(event3), 0L))
            .isInstanceOf(ConcurrencyException.class);
        assertThat(sut.getEventsForAggregate(id)).contains(event, event2);
    }

    @Test
    public void publishesSavedEvents() {
        sut.saveEvents(id, Arrays.asList(event, event2), -1L);

        verify(eventPublisher).publish(event);
        verify(eventPublisher).publish(event2);
    }

}
