package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public class PublishDocumentCommand implements Command {
    @NotNull
    public UUID documentId;
    @NotNull
    public Long employeeId;
    @NotNull
    @NotEmpty
    public Set<Long> departmentIds;
    public boolean includeDepartmentsFromPreviousVersion;
    @NotNull
    public Long aggregateVersion;

    public PublishDocumentCommand(UUID documentId, Long employeeId, Set<Long> departmentIds) {
        this(documentId, employeeId, departmentIds, false);
    }

    public PublishDocumentCommand(UUID documentId, Long employeeId, Set<Long> departmentIds, boolean includeDepartmentsFromPreviousVersion) {
        this.documentId = documentId;
        this.employeeId = employeeId;
        this.departmentIds = departmentIds;
        this.includeDepartmentsFromPreviousVersion = includeDepartmentsFromPreviousVersion;
    }

    public PublishDocumentCommand() {
    }
}
