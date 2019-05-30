package pl.com.bottega.docflowjee.docflow.adapters.repository;

import pl.com.bottega.docflowjee.docflow.Document;
import pl.com.bottega.docflowjee.docflow.DocumentRepository;
import pl.com.bottega.docflowjee.docflow.EmployeePermissionsPolicy;
import pl.com.bottega.eventsourcing.EventStoreRepository;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

public class EventStoreDocumentRepository implements DocumentRepository {

    private final EventStoreRepository<Document> repository;
    private Clock clock;
    private EmployeePermissionsPolicy employeePermissionsPolicy;

    public EventStoreDocumentRepository(EventStoreRepository<Document> repository, Clock clock, EmployeePermissionsPolicy employeePermissionsPolicy) {
        this.repository = repository;
        this.clock = clock;
        this.employeePermissionsPolicy = employeePermissionsPolicy;
    }

    @Override
    public Document get(UUID id) {
        Document document = repository.get(id);
        injectDependencies(document);
        return document;
    }

    private void injectDependencies(Document document) {
        document.setClock(clock);
        document.setEmployeePermissionsPolicy(employeePermissionsPolicy);
    }

    @Override
    public Optional<Document> getOptionally(UUID id) {
        return repository.getOptionally(id).map(document -> {
            injectDependencies(document);
            return document;
        });
    }

    @Override
    public void save(Document aggregateRoot, Long expectedVersion) {
        repository.save(aggregateRoot, expectedVersion);
    }
}
