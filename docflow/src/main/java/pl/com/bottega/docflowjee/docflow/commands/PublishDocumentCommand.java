package pl.com.bottega.docflowjee.docflow.commands;

import pl.com.bottega.eventsourcing.Command;

import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class PublishDocumentCommand implements Command {
    private UUID documentId;
    private Long employeeId;
    private Set<Long> departmentIds;
    private boolean includeDepartmentsFromPreviousVersion;
    private Long aggregateVersion;

    public PublishDocumentCommand(UUID documentId, Long employeeId, Set<Long> departmentIds, Long aggregateVersion) {
        this(documentId, employeeId, departmentIds, false, aggregateVersion);
    }

    public PublishDocumentCommand(UUID documentId, Long employeeId, Set<Long> departmentIds, boolean includeDepartmentsFromPreviousVersion, Long aggregateVersion) {
        checkNotNull(documentId);
        checkNotNull(employeeId);
        checkNotNull(aggregateVersion);
        checkNotNull(departmentIds);
        checkArgument(departmentIds.size() > 0);
        this.documentId = documentId;
        this.employeeId = employeeId;
        this.departmentIds = departmentIds;
        this.includeDepartmentsFromPreviousVersion = includeDepartmentsFromPreviousVersion;
        this.aggregateVersion = aggregateVersion;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Set<Long> getDepartmentIds() {
        return departmentIds;
    }

    public boolean isIncludeDepartmentsFromPreviousVersion() {
        return includeDepartmentsFromPreviousVersion;
    }

    public Long getAggregateVersion() {
        return aggregateVersion;
    }
}
