package pl.com.bottega.docflowjee.confirmations.adapters.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.com.bottega.docflowjee.confirmations.domain.Confirmation;
import pl.com.bottega.docflowjee.confirmations.domain.ConfirmationRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class SpringDataConfirmationRepository implements ConfirmationRepository {

    private MongoConfirmationRepository mongoConfirmationRepository;

    @Override
    public Mono<Void> save(List<Confirmation> confirmations) {
        return mongoConfirmationRepository.saveAll(
            confirmations.stream().map(this::toMongoConfirmation).collect(toList())
        );
    }

    @Override
    public Mono<Void> save(Confirmation confirmation) {
        return mongoConfirmationRepository.save(toMongoConfirmation(confirmation));
    }

    @Override
    public Mono<Confirmation> get(UUID documentId, Long employeeId) {
        return mongoConfirmationRepository
            .findByDocumentIdAndEmployeeId(documentId.toString(), employeeId)
            .map(this::toConfirmation);
    }

    private Confirmation toConfirmation(MongoConfirmation confirmation) {
        return new Confirmation(UUID.fromString(confirmation.documentId), confirmation.employeeId, confirmation.confirmingEmployeeId, confirmation.confirmed);
    }

    private MongoConfirmation toMongoConfirmation(Confirmation confirmation) {
        return MongoConfirmation.builder()
            .confirmed(confirmation.isConfirmed())
            .confirmingEmployeeId(confirmation.getConfirmingEmployeeId())
            .employeeId(confirmation.getEmployeeId())
            .documentId(confirmation.getDocumentId().toString())
            .build();
    }

}

@Document
@Builder
class MongoConfirmation {
    @Id
    String id;
    String documentId;
    Long employeeId;
    Long confirmingEmployeeId;
    boolean confirmed;
}

