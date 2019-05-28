package pl.com.bottega.docflowjee.docflow.adapters.repository;

import pl.com.bottega.docflowjee.docflow.Document;
import pl.com.bottega.docflowjee.docflow.DocumentRepository;
import pl.com.bottega.eventsourcing.EventStoreRepository;

import java.time.Clock;
import java.util.UUID;

public class EventStoreDocumentRepository implements DocumentRepository {

    private final EventStoreRepository<Document> repository;
    private Clock clock;

    public EventStoreDocumentRepository(EventStoreRepository<Document> repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public Document get(UUID id) {
        Document document = repository.get(id);
        document.setClock(clock);
        return document;
    }

    @Override
    public void save(Document aggregateRoot, Long expectedVersion) {
        repository.save(aggregateRoot, expectedVersion);
    }
}
