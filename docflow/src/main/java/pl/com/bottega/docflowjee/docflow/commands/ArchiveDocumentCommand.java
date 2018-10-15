package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class ArchiveDocumentCommand implements Command {
    @NotNull
    public UUID documentId;
    @NotNull
    public Long employeeId;
    @NotNull
    public Long aggregateVersion;

    public ArchiveDocumentCommand(UUID documentId, Long employeeId) {
        this.documentId = documentId;
        this.employeeId = employeeId;
    }

    public ArchiveDocumentCommand() {
    }
}
