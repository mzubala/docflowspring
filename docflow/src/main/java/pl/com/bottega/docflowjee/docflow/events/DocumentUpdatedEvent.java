package pl.com.bottega.docflowjee.docflow.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class DocumentUpdatedEvent extends Event {
    private String title;
    private String content;
    private Integer version;
    private Long employeeId;

    @JsonCreator
    public DocumentUpdatedEvent(@JsonProperty("id") UUID id,
                                @JsonProperty("employeeId") Long employeeId,
                                @JsonProperty("createdAt") Instant createdAt,
                                @JsonProperty("title") String title,
                                @JsonProperty("content") String content,
                                @JsonProperty("version") Integer version) {
        super(id, createdAt);
        this.title = title;
        this.content = content;
        this.version = version;
        this.employeeId = employeeId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return title;
    }

    public Long getEmployeeId() {
        return employeeId;
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
