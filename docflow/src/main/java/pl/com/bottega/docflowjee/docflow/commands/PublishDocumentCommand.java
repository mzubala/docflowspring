package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.Set;
import java.util.UUID;

public class PublishDocumentCommand implements Command {
    public UUID documentId;
    public Long employeeId;
    public Set<Long> departmentIds;
    public boolean includeDepartmentsFromPreviousVersion;
}
