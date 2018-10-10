package pl.com.bottega.eventsourcing.testdata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class TestValueChangedEvent extends Event {

    private final String value;

    @JsonCreator
    public TestValueChangedEvent(@JsonProperty("aggregateId") UUID aggregateId,
                                 @JsonProperty("createdAt") Instant createdAt,
                                 @JsonProperty("value") String value) {
        super(aggregateId, createdAt);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TestValueChangedEvent that = (TestValueChangedEvent) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
