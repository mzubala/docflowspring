package pl.com.bottega.docflowjee.confirmations.adapters.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.com.bottega.docflowjee.confirmations.domain.Confirmation;
import pl.com.bottega.docflowjee.confirmations.domain.ConfirmationRepository;
import reactor.core.publisher.Mono;

import java.io.Serializable;
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
        return mongoConfirmationRepository.save(toMongoConfirmation(confirmation)).then();
    }

    @Override
    public Mono<Confirmation> findFor(UUID documentId, Long employeeId) {
        return mongoConfirmationRepository.findById(new MongoConfirmationId(documentId.toString(), employeeId)).map(
            this::fromMongoConfirmation
        );
    }

    private Confirmation fromMongoConfirmation(MongoConfirmation mongoConfirmation) {
        return new Confirmation(
            UUID.fromString(mongoConfirmation.id.documentId),
            mongoConfirmation.id.employeeId,
            mongoConfirmation.confirmingEmployeeId,
            mongoConfirmation.confirmed
        );
    }

    private MongoConfirmation toMongoConfirmation(Confirmation confirmation) {
        return MongoConfirmation.builder()
            .confirmed(confirmation.isConfirmed())
            .confirmingEmployeeId(confirmation.getConfirmingEmployeeId())
            .id(
                MongoConfirmationId.builder().documentId(confirmation.getDocumentId().toString())
                    .employeeId(confirmation.getEmployeeId()).build()
            )
            .build();
    }

}

@Document
@Builder
class MongoConfirmation {
    @Id
    MongoConfirmationId id;
    Long confirmingEmployeeId;
    boolean confirmed;
}

@Builder
class MongoConfirmationId implements Serializable {
    String documentId;
    Long employeeId;
}

