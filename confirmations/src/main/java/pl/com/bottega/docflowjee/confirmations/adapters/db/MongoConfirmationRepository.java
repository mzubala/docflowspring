package pl.com.bottega.docflowjee.confirmations.adapters.db;

import org.springframework.data.repository.Repository;
import reactor.core.publisher.Mono;

public interface MongoConfirmationRepository extends Repository<MongoConfirmation, String> {

    Mono<Void> save(Iterable<MongoConfirmation> toSave);

}
