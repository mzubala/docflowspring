package pl.com.bottega.docflowjee.docflow.events;

import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class NewDocumentVersionCreatedEvent extends Event {
    private Integer version;

    public NewDocumentVersionCreatedEvent(UUID id, Instant instant, Integer version) {
        super(id, instant);
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NewDocumentVersionCreatedEvent that = (NewDocumentVersionCreatedEvent) o;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), version);
    }

    @Override
    public String toString() {
        return "NewDocumentVersionCreatedEvent{" +
            "version=" + version +
            "} " + super.toString();
    }
}
