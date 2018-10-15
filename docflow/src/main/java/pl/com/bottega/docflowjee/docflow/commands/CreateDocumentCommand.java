package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class CreateDocumentCommand implements Command {

    @NotNull
    public UUID documentId;
    @NotNull
    public Long employeeId;

    public CreateDocumentCommand(UUID documentId, Long employeeId) {
        this.documentId = documentId;
        this.employeeId = employeeId;
    }

    public CreateDocumentCommand() {
    }
}
