package pl.com.bottega.docflowjee.docflow.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.com.bottega.docflowjee.docflow.Document;
import pl.com.bottega.eventsourcing.EventPublisher;
import pl.com.bottega.eventsourcing.EventStore;
import pl.com.bottega.eventsourcing.EventStoreRepository;
import pl.com.bottega.docflowjee.docflow.adapters.jms.JMSPublisher;
import pl.com.bottega.eventsourcing.jpa.JPAEventStore;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Clock;

public class Resources {

    @PersistenceContext
    @Produces
    private EntityManager em;


    //private JMSContext jmsContext;

    @Produces
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Produces
    public EventStoreRepository<Document> eventStoreRepository(EventStore eventStore) {
        return new EventStoreRepository<>(eventStore, Document.class);
    }

    @Produces
    public EventStore eventStore(ObjectMapper mapper, EventPublisher publisher) {
        return new JPAEventStore(mapper, publisher, em);
    }

    @Produces
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }


}
