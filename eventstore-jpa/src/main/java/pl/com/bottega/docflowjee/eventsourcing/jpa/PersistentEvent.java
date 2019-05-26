package pl.com.bottega.docflowjee.eventsourcing.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events", indexes = @Index(columnList = "aggregateId"))
@NamedQuery(name = "PersistentEvent.forAggregate",
    query = "SELECT e FROM PersistentEvent e WHERE e.aggregateId = :id"
)
@NamedQuery(name = "PersistentEvent.lastVersion",
    query = "SELECT MAX(e.version) FROM PersistentEvent e WHERE e.aggregateId = :id"
)
class PersistentEvent {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 1 << 16)
    String payload;

    String eventClass;

    UUID aggregateId;

    Instant timestamp;

    Long version;

}
