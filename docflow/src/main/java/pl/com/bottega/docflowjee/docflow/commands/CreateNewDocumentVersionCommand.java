package pl.com.bottega.docflowjee.docflow.commands;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateNewDocumentVersionCommand {
    private UUID documentId;
    private Long employeeId;
    private Long aggregateVersion;

    public CreateNewDocumentVersionCommand(UUID documentId, Long employeeId, Long aggregateVersion) {
        checkNotNull(documentId);
        checkNotNull(employeeId);
        checkNotNull(aggregateVersion);
        this.documentId = documentId;
        this.employeeId = employeeId;
        this.aggregateVersion = aggregateVersion;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Long getAggregateVersion() {
        return aggregateVersion;
    }
}
