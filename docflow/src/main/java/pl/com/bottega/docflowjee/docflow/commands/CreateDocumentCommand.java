package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

public class CreateDocumentCommand implements Command {

    public UUID documentId;
    public Long employeeId;

    public CreateDocumentCommand(UUID documentId, Long employeeId) {
        this.documentId = documentId;
        this.employeeId = employeeId;
    }

    public CreateDocumentCommand() {
    }
}
