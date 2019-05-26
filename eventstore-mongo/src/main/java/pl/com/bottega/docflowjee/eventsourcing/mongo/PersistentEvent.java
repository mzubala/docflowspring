package pl.com.bottega.docflowjee.eventsourcing.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "events")
class PersistentEvent {

    @Id
    private String id;

    Event payload;

    @Indexed(unique = false)
    UUID aggregateId;

    Instant timestamp;

    Long version;

}
