package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

public class UpdateDocumentCommand implements Command {

    public UUID documentId;
    public Long employeeId;
    public String title;
    public String content;

}
