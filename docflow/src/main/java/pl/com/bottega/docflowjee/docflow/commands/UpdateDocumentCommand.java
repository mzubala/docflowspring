package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

public class UpdateDocumentCommand implements Command {

    public UUID documentId;
    public Long employeeId;
    public String title;
    public String content;
    public Long aggregateVersion;

    public UpdateDocumentCommand() {
    }

    public UpdateDocumentCommand(UUID documentId, Long employeeId, String title, String content) {
        this.documentId = documentId;
        this.employeeId = employeeId;
        this.title = title;
        this.content = content;
    }
}
