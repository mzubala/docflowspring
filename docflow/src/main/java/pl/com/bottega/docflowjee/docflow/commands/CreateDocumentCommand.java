package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

public class CreateDocumentCommand implements Command {

    public UUID documentId;
    public Long employeeId;

}
