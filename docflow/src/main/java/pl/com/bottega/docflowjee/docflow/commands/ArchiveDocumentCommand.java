package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

public class ArchiveDocumentCommand implements Command {
    private UUID documentId;
    private Long employeeId;
    private Long aggregateVersion;

    public ArchiveDocumentCommand(UUID documentId, Long employeeId, Long aggregateVersion) {
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
