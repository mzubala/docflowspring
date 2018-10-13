package pl.com.bottega.eventsourcing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public void loadFromHistory(List<Event> history) {
        history.forEach(it -> applyChange(it, false));
    }

    protected void applyChange(Event event) {
        applyChange(event, true);
    }

    protected void applyChange(Event event, boolean isNew) {
        dispatch(event);
        if (isNew) {
            changes.add(event);
        }
    }

    protected void eventNotSupported(Event event) {
        throw new IllegalArgumentException(String.format("Event {} is not supported", event));
    }

    protected void dispatch(Event event) {
        try {
            Method applyMethod = getClass().getDeclaredMethod("apply", event.getClass());
            applyMethod.setAccessible(true);
            applyMethod.invoke(this, event);
            applyMethod.setAccessible(false);
        } catch(InvocationTargetException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw noApplyMethodError(event, ex);
        }
    }

    private IllegalArgumentException noApplyMethodError(Event event, NoSuchMethodException e) {
        return new IllegalArgumentException(String.format("No method void apply(%s event) found in the aggregate %s",
            event.getClass().getName(), getClass().getName()), e);
    }

}
