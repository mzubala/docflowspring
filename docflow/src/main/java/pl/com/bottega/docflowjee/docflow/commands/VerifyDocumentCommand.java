package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class VerifyDocumentCommand implements Command {
    private UUID documentId;
    private Long employeeId;
    private Long aggregateVersion;

    public VerifyDocumentCommand(UUID documentId, Long employeeId, Long aggregateVersion) {
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

    public Long getAggregateVersion() {
        return aggregateVersion;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
}
