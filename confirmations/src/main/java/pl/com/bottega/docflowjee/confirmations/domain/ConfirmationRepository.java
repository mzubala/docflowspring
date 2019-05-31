package pl.com.bottega.docflowjee.confirmations.domain;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ConfirmationRepository {
    Mono<Void> save(List<Confirmation> confirmations);
}
