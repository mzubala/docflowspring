package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.docflowjee.docflow.commands.validation.ValidateObscenity;
import pl.com.bottega.eventsourcing.Command;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class UpdateDocumentCommand implements Command {
    @NotNull
    public UUID documentId;
    @NotNull
    public Long employeeId;
    @NotEmpty
    @ValidateObscenity
    public String title;
    @NotEmpty
    @ValidateObscenity
    public String content;
    @NotNull
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
