package pl.com.bottega.docflowjee.confirmations.adapters.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.Repository;
import pl.com.bottega.docflowjee.confirmations.domain.Confirmation;
import pl.com.bottega.docflowjee.confirmations.domain.ConfirmationRepository;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class SpringDataConfirmationRepository implements ConfirmationRepository {

    private MongoConfirmationRepository mongoConfirmationRepository;

    @Override
    public Mono<Void> save(List<Confirmation> confirmations) {
        return mongoConfirmationRepository.save(
            confirmations.stream().map(this::toMongoConfirmation).collect(toList())
        );
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
    private String id;
    private String documentId;
    private Long employeeId;
    private Long confirmingEmployeeId;
    private boolean confirmed;
}

