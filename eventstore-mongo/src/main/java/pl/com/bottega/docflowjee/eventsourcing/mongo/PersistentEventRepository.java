package pl.com.bottega.docflowjee.eventsourcing.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface PersistentEventRepository extends MongoRepository<PersistentEvent, String> {
    List<PersistentEvent> findByAggregateId(UUID aggregateId);

    Optional<PersistentEvent> findFirstByAggregateIdOrderByTimestampDesc(UUID aggregateId);
}
