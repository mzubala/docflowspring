package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.Set;
import java.util.UUID;

public class PublishDocumentCommand implements Command {
    public UUID documentId;
    public Long employeeId;
    public Set<Long> departmentIds;
    public boolean includeDepartmentsFromPreviousVersion;

    public PublishDocumentCommand(UUID documentId, Long employeeId, Set<Long> departmentIds) {
        this(documentId, employeeId, departmentIds, false);
    }

    public PublishDocumentCommand(UUID documentId, Long employeeId, Set<Long> departmentIds, boolean includeDepartmentsFromPreviousVersion) {
        this.documentId = documentId;
        this.employeeId = employeeId;
        this.departmentIds = departmentIds;
        this.includeDepartmentsFromPreviousVersion = includeDepartmentsFromPreviousVersion;
    }
}
