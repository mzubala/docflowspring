package pl.com.bottega.docflowjee.eventsourcing.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.eventsourcing.ConcurrencyException;
import pl.com.bottega.eventsourcing.Event;
import pl.com.bottega.eventsourcing.EventPublisher;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class JPAEventStoreTest {

    private JPAEventStore sut;
    private final UUID id = UUID.randomUUID();
    private final UUID otherId = UUID.randomUUID();
    private final TestAggregateCreatedEvent event = new TestAggregateCreatedEvent(id, Instant.now());
    private final TestValueChangedEvent event2 = new TestValueChangedEvent(id, Instant.now(), "val2");
    private final TestValueChangedEvent event3 = new TestValueChangedEvent(id, Instant.now(), "val3");
    private final TestValueChangedEvent otherAggEvent = new TestValueChangedEvent(otherId, Instant.now(), "val");
    private EntityManager entityManager;


    @Before
    public void before() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        sut = new JPAEventStore(mapper, mock(EventPublisher.class), entityManager);
    }


    @Test
    public void storesSingleEvent() {
        executeInTx(() -> sut.saveEvents(id, Arrays.asList(event), -1L));

        List<Event> fetched = sut.getEventsForAggregate(id);

        assertThat(fetched).containsExactly(event);
    }

    @Test
    public void storesMultipleEvents() {
        executeInTx(() -> sut.saveEvents(id, Arrays.asList(event, event2, event3), -1L));

        List<Event> fetched = sut.getEventsForAggregate(id);

        assertThat(fetched).containsExactly(event, event2, event3);
    }

    @Test
    public void storesEventsForMultipleAggregates() {
        executeInTx(() -> sut.saveEvents(id, Arrays.asList(event, event2), -1L));
        executeInTx(() -> sut.saveEvents(otherId, Arrays.asList(otherAggEvent), -1L));

        List<Event> fetched = sut.getEventsForAggregate(id);
        List<Event> fetchedOtherAgg = sut.getEventsForAggregate(otherId);

        assertThat(fetched).containsExactly(event, event2);
        assertThat(fetchedOtherAgg).containsExactly(otherAggEvent);
    }

    @Test
    public void storesEventVersion() {
        executeInTx(() -> sut.saveEvents(id, Arrays.asList(event, event2), -1L));
        executeInTx(() -> sut.saveEvents(id, Arrays.asList(event3), 1L));

        List<Event> fetched = sut.getEventsForAggregate(id);

        assertThat(fetched.stream().map(e -> e.getAggregateVersion()).collect(Collectors.toList()))
            .contains(0L, 1L, 2L);
    }

    @Test
    public void providesOptimisticLocking() {
        executeInTx(() -> sut.saveEvents(id, Arrays.asList(event, event2), -1L));

        assertThatThrownBy(() -> executeInTx(() -> sut.saveEvents(id, Arrays.asList(event3), 0L)))
            .isInstanceOf(ConcurrencyException.class);
        assertThat(sut.getEventsForAggregate(id)).contains(event, event2);
    }

    private void executeInTx(Runnable runnable) {
        try {
            entityManager.getTransaction().begin();
            runnable.run();
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (RuntimeException ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }

    }

}
