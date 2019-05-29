package pl.com.bottega.docflowjee.docflow.commands;

import org.apache.commons.lang3.StringUtils;
import pl.com.bottega.eventsourcing.Command;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class RejectDocumentCommand implements Command {
    private UUID documentId;
    private Long employeeId;
    private String reason;
    private Long aggregateVersion;

    public RejectDocumentCommand(UUID id, Long employeeId, String reason, Long aggregateVersion) {
        checkNotNull(id);
        checkNotNull(employeeId);
        checkNotNull(aggregateVersion);
        checkArgument(isNotEmpty(reason));
        this.documentId = id;
        this.employeeId = employeeId;
        this.reason = reason;
        this.aggregateVersion = aggregateVersion;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getReason() {
        return reason;
    }

    public Long getAggregateVersion() {
        return aggregateVersion;
    }
}
