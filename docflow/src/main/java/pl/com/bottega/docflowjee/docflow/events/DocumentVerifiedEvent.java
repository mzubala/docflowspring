package pl.com.bottega.docflowjee.docflow.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class DocumentVerifiedEvent extends Event {
    private Integer version;

    @JsonCreator
    public DocumentVerifiedEvent(@JsonProperty("aggregateId") UUID aggregateId,
                                 @JsonProperty("createdAt") Instant createdAt,
                                 @JsonProperty("version") Integer version) {
        super(aggregateId, createdAt);
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DocumentVerifiedEvent that = (DocumentVerifiedEvent) o;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), version);
    }

    @Override
    public String toString() {
        return "DocumentVerifiedEvent{" +
            "version=" + version +
            "} " + super.toString();
    }
}
