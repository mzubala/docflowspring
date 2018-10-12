package pl.com.bottega.docflowjee.docflow.events;

import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class DocumentArchivedEvent extends Event {
    private Integer firstVersion;

    public DocumentArchivedEvent(UUID aggregateId, Instant createdAt, Integer firstVersion) {
        super(aggregateId, createdAt);
        this.firstVersion = firstVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DocumentArchivedEvent that = (DocumentArchivedEvent) o;
        return Objects.equals(firstVersion, that.firstVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstVersion);
    }
}
