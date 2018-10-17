package pl.com.bottega.docflowjee.docflow.adapters.repository;

import pl.com.bottega.docflowjee.docflow.Document;
import pl.com.bottega.docflowjee.docflow.DocumentRepository;
import pl.com.bottega.eventsourcing.EventStoreRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

public class EventStoreDocumentRepository implements DocumentRepository {

    private final EventStoreRepository<Document> repository;
    private Clock clock;

    @Inject
    public EventStoreDocumentRepository(EventStoreRepository<Document> repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public Document get(UUID id) {
        Document document = repository.get(id);
        injectDependencies(document);
        return document;
    }

    private void injectDependencies(Document document) {
        document.setClock(clock);
    }

    @Override
    public Optional<Document> getOptionally(UUID id) {
        return repository.getOptionally(id).map(document -> {
            injectDependencies(document);
            return document;
        });
    }

    @Override
    @Transactional
    public void save(Document aggregateRoot, Long expectedVersion) {
        repository.save(aggregateRoot, expectedVersion);
    }
}
