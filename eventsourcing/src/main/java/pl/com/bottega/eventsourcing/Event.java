package pl.com.bottega.eventsourcing;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public abstract class Event {

    private UUID aggregateId;
    private Instant createdAt;
    private Long aggregateVersion;

    public Event(UUID aggregateId, Instant createdAt) {
        this.aggregateId = aggregateId;
        this.createdAt = createdAt;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getAggregateVersion() {
        return aggregateVersion;
    }

    public void setAggregateVersion(Long aggregateVersion) {
        this.aggregateVersion = aggregateVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(aggregateId, event.aggregateId) &&
            Objects.equals(createdAt, event.createdAt) &&
            Objects.equals(aggregateVersion, event.aggregateVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aggregateId, createdAt, aggregateVersion);
    }

    @Override
    public String toString() {
        return "Event{" +
            "aggregateId=" + aggregateId +
            ", createdAt=" + createdAt +
            ", aggregateVersion=" + aggregateVersion +
            '}';
    }
}
