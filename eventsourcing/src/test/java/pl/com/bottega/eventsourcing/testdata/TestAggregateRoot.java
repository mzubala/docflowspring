package pl.com.bottega.eventsourcing.testdata;

import pl.com.bottega.eventsourcing.AggregateRoot;

import java.time.Clock;
import java.util.UUID;

public class TestAggregateRoot extends AggregateRoot {

    private String testValue = "initial";

    TestAggregateRoot() {}

    public TestAggregateRoot(UUID id, Clock clock) {
        emmit(new TestAggregateCreatedEvent(id, clock.instant()));
    }

    public void changeState(String newState, Clock clock) {
        if(newState == null || newState.equals(testValue)) {
            throw new IllegalArgumentException();
        }
        emmit(new TestValueChangedEvent(this.id, clock.instant(), newState));
    }

    private void apply(TestAggregateCreatedEvent event) {
        this.id = event.getAggregateId();
    }

    private void apply(TestValueChangedEvent event) {
        this.testValue = event.getValue();
    }

    public String getState() {
        return testValue;
    }
}
