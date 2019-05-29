package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateDocumentCommand implements Command {

    private UUID documentId;
    private Long employeeId;
    private String title;
    private String content;
    private Long aggregateVersion;

    public UpdateDocumentCommand(UUID documentId, Long employeeId, String title, String content, Long aggregateVersion) {
        checkNotNull(documentId);
        checkNotNull(employeeId);
        checkNotNull(aggregateVersion);
        this.documentId = documentId;
        this.employeeId = employeeId;
        this.title = title;
        this.content = content;
        this.aggregateVersion = aggregateVersion;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Long getAggregateVersion() {
        return aggregateVersion;
    }
}
