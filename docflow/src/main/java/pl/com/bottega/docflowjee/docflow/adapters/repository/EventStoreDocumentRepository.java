package pl.com.bottega.docflowjee.docflow.adapters.repository;

import pl.com.bottega.docflowjee.docflow.Document;
import pl.com.bottega.docflowjee.docflow.DocumentRepository;
import pl.com.bottega.eventsourcing.EventStoreRepository;

import java.util.UUID;

public class EventStoreDocumentRepository implements DocumentRepository {

    private final EventStoreRepository<Document> repository;

    public EventStoreDocumentRepository(EventStoreRepository<Document> repository) {
        this.repository = repository;
    }

    @Override
    public Document get(UUID id) {
        return repository.get(id);
    }

    @Override
    public void save(Document aggregateRoot, Long expectedVersion) {
        repository.save(aggregateRoot, expectedVersion);
    }
}
