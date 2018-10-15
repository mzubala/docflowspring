package pl.com.bottega.docflowjee.docflow.commands;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class PassToVerificationCommand {
    @NotNull
    public UUID documentId;
    @NotNull
    public Long employeeId;
    @NotNull
    public Long aggregateVersion;

    public PassToVerificationCommand(UUID documentId, Long employeeId) {
        this.documentId = documentId;
        this.employeeId = employeeId;
    }

    public PassToVerificationCommand() {
    }
}
