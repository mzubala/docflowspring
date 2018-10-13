package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

public class VerifyDocumentCommand implements Command {
    private UUID documentId;
    private Long employeeId;

    public VerifyDocumentCommand(UUID id, Long employeeId) {
        this.documentId = id;
        this.employeeId = employeeId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
}
