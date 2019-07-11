package pl.com.bottega.docflowjee.confirmations.domain;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ConfirmationRepository {
    Mono<Void> save(List<Confirmation> confirmations);

    Mono<Void> save(Confirmation confirmations);

    Mono<Confirmation> get(UUID documentId, Long employeeId);
}
