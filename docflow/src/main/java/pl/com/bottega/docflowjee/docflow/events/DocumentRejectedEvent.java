package pl.com.bottega.docflowjee.docflow.events;

import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class DocumentRejectedEvent extends Event {
    private String reason;
    private Integer version;

    public DocumentRejectedEvent(UUID id, Instant instant, String reason, Integer version) {
        super(id, instant);
        this.reason = reason;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DocumentRejectedEvent that = (DocumentRejectedEvent) o;
        return Objects.equals(reason, that.reason) &&
            Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), reason, version);
    }
}
