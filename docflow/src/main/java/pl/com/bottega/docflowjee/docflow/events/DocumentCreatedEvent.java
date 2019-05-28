package pl.com.bottega.docflowjee.docflow.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class DocumentCreatedEvent extends Event {
    private final Long employeeId;

    @JsonCreator
    public DocumentCreatedEvent(@JsonProperty("aggregateId") UUID aggregateId,
                                @JsonProperty("createdAt") Instant createdAt,
                                @JsonProperty("employeeId") Long employeeId) {
        super(aggregateId, createdAt);
        this.employeeId = employeeId;
    }

    public Long getEmployeeId() {
        return employeeId;
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

    @Override
    public String toString() {
        return "DocumentCreatedEvent{" +
            "employeeId=" + employeeId +
            "} " + super.toString();
    }
}
