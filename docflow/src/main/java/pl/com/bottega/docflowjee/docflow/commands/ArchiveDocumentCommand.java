package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

public class ArchiveDocumentCommand implements Command {
    public UUID documentId;
    public Long employeeId;
    public Long aggregateVersion;

    public ArchiveDocumentCommand(UUID documentId, Long employeeId) {
        this.documentId = documentId;
        this.employeeId = employeeId;
    }

    public ArchiveDocumentCommand() {
    }
}
