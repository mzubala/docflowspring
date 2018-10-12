package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

public class VerifyDocumentCommand implements Command {
    public UUID documentId;
    public Long employeeId;

    public VerifyDocumentCommand(UUID id, Long employeeId) {
        this.documentId = id;
        this.employeeId = employeeId;
    }
}
