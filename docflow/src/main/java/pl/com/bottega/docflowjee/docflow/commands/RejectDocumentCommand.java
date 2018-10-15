package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class RejectDocumentCommand implements Command {
    @NotNull
    public UUID documentId;
    @NotNull
    public Long employeeId;
    @NotEmpty
    public String reason;
    @NotNull
    public Long aggregateVersion;

    public RejectDocumentCommand(UUID id, Long employeeId, String reason) {
        this.documentId = id;
        this.employeeId = employeeId;
        this.reason = reason;
    }

    public RejectDocumentCommand() {
    }
}
