package pl.com.bottega.docflowjee.docflow.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class DocumentPassedToVerification extends Event {
    private Integer version;

    @JsonCreator
    public DocumentPassedToVerification(@JsonProperty("id") UUID id,
                                        @JsonProperty("createdAt") Instant createdAt,
                                        @JsonProperty("version") Integer version) {
        super(id, createdAt);
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DocumentPassedToVerification that = (DocumentPassedToVerification) o;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), version);
    }

    @Override
    public String toString() {
        return "DocumentPassedToVerification{" +
            "version=" + version +
            "} " + super.toString();
    }
}
