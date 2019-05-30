package pl.com.bottega.docflowjee.docflow.adapters.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import pl.com.bottega.docflowjee.docflow.Document;
import pl.com.bottega.docflowjee.docflow.DocumentPreparation;
import pl.com.bottega.docflowjee.docflow.DocumentPublication;
import pl.com.bottega.docflowjee.docflow.DocumentRepository;
import pl.com.bottega.docflowjee.docflow.DocumentVerification;
import pl.com.bottega.docflowjee.docflow.EmployeePermissionsPolicy;
import pl.com.bottega.docflowjee.docflow.HrEmployeePermissionsPolicy;
import pl.com.bottega.docflowjee.docflow.HrFacade;
import pl.com.bottega.docflowjee.docflow.adapters.client.HrClient;
import pl.com.bottega.docflowjee.docflow.adapters.client.RestClientHrFacade;
import pl.com.bottega.docflowjee.docflow.adapters.jms.JMSPublisher;
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
    public EventPublisher eventPublisher(JmsTemplate jmsTemplate) {
        return new JMSPublisher(jmsTemplate);
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
    public HrFacade hrFacade(HrClient hrClient) {
        return new RestClientHrFacade(hrClient);
    }

    @Bean
    public EmployeePermissionsPolicy employeePermissionsPolicy(HrFacade hrFacade) {
        return new HrEmployeePermissionsPolicy(hrFacade);
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

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        var mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        converter.setObjectMapper(mapper);
        return converter;
    }

}
