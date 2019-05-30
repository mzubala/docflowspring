package pl.com.bottega.docflowjee.docflow.adapters.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.bottega.docflowjee.docflow.Document;
import pl.com.bottega.docflowjee.docflow.DocumentPreparation;
import pl.com.bottega.docflowjee.docflow.DocumentPublication;
import pl.com.bottega.docflowjee.docflow.DocumentRepository;
import pl.com.bottega.docflowjee.docflow.DocumentVerification;
import pl.com.bottega.docflowjee.docflow.EmployeePermissionsPolicy;
import pl.com.bottega.docflowjee.docflow.HrEmployeePermissionsPolicy;
import pl.com.bottega.docflowjee.docflow.adapters.repository.EventStoreDocumentRepository;
import pl.com.bottega.eventsourcing.EventPublisher;
import pl.com.bottega.eventsourcing.EventStore;
import pl.com.bottega.eventsourcing.EventStoreRepository;

import java.time.Clock;

@Configuration
public class DocflowConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public EventPublisher eventPublisher() {
        return event -> {
        };
    }

    @Bean
    public EventStoreRepository<Document> eventStoreRepository(EventStore store) {
        return new EventStoreRepository<>(store, Document.class);
    }

    @Bean
    public DocumentRepository documentRepository(EventStoreRepository<Document> eventStoreRepository, Clock clock, EmployeePermissionsPolicy employeePermissionsPolicy) {
        return new EventStoreDocumentRepository(eventStoreRepository, clock, employeePermissionsPolicy);
    }

    @Bean
    public EmployeePermissionsPolicy employeePermissionsPolicy() {
        return new HrEmployeePermissionsPolicy();
    }

    @Bean
    public DocumentPreparation documentPreparation(DocumentRepository documentRepository, Clock clock, EmployeePermissionsPolicy employeePermissionsPolicy) {
        return new DocumentPreparation(documentRepository, employeePermissionsPolicy, clock);
    }

    @Bean
    public DocumentVerification documentVerification(DocumentRepository documentRepository) {
        return new DocumentVerification(documentRepository);
    }

    @Bean
    public DocumentPublication documentPublication(DocumentRepository documentRepository) {
        return new DocumentPublication(documentRepository);
    }

}
