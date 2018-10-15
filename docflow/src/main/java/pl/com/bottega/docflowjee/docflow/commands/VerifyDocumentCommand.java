package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class VerifyDocumentCommand implements Command {
    @NotNull
    public UUID documentId;
    @NotNull
    public Long employeeId;
    @NotNull
    public Long aggregateVersion;

    public VerifyDocumentCommand(UUID id, Long employeeId) {
        this.documentId = id;
        this.employeeId = employeeId;
    }

    public VerifyDocumentCommand() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }
}
