package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateDocumentCommand implements Command {

    private UUID documentId;
    private Long employeeId;

    public CreateDocumentCommand(UUID documentId, Long employeeId) {
        checkNotNull(documentId);
        checkNotNull(employeeId);
        this.documentId = documentId;
        this.employeeId = employeeId;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
}
