package pl.com.bottega.docflowjee.docflow.events;

import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class DocumentCreatedEvent extends Event {
    private final Long employeeId;

    public DocumentCreatedEvent(UUID id, Instant instant, Long employeeId) {
        super(id, instant);
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DocumentCreatedEvent that = (DocumentCreatedEvent) o;
        return Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employeeId);
    }
}
