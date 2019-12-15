package pl.com.bottega.docflowjee.confirmations.adapters.db;

import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface MongoConfirmationRepository extends Repository<MongoConfirmation, String> {

    Mono<Void> saveAll(Iterable<MongoConfirmation> toSave);

    Flux<MongoConfirmation> findAll();

    Mono<Long> count();

    Mono<MongoConfirmation> findById(MongoConfirmationId confirmationId);

    Mono<MongoConfirmation> save(MongoConfirmation mongoConfirmation);
}
