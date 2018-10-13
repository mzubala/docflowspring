package pl.com.bottega.docflowjee.docflow.events;

import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class DocumentUpdatedEvent extends Event {
    private String title;
    private String content;
    private Integer version;

    public DocumentUpdatedEvent(UUID id, Instant instant, String title, String content, Integer version) {
        super(id, instant);
        this.title = title;
        this.content = content;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DocumentUpdatedEvent that = (DocumentUpdatedEvent) o;
        return Objects.equals(title, that.title) &&
            Objects.equals(content, that.content) &&
            Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, content, version);
    }

    @Override
    public String toString() {
        return "DocumentUpdatedEvent{" +
            "title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", version=" + version +
            "} " + super.toString();
    }
}
