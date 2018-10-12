package pl.com.bottega.eventsourcing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class AggregateRoot {

    protected UUID id;
    private List<Event> changes = new LinkedList<>();
    private Long version;

    public UUID getId() {
        return id;
    }

    public List<Event> getUncommitedEvents() {
        return Collections.unmodifiableList(changes);
    }

    public void markChangesCommited() {
        this.changes.clear();
    }

    void loadFromHistory(List<Event> history) {
        history.forEach(it -> applyChange(it, false));
    }

    protected void applyChange(Event event) {
        applyChange(event, true);
    }

    protected void applyChange(Event event, boolean isNew) {
        dispatch(event);
        if(isNew) {
            changes.add(event);
        }
    }

    protected void eventNotSupported(Event event) {
        throw new IllegalArgumentException(String.format("Event {} is not supported", event));
    }

    protected abstract void dispatch(Event event);

}
