package pl.com.bottega.docflowjee.docflow.adapters.rest;

import java.util.Set;

public class PublishDocumentRequest extends DocumentRequest {

    public Set<Long> departmentIds;
    public boolean includeDepartmentsFromPreviousVersion;

    public PublishDocumentRequest(Long employeeId, Long aggregateVersion, Set<Long> departmentIds, boolean includeDepartmentsFromPreviousVersion) {
        super(employeeId, aggregateVersion);
        this.departmentIds = departmentIds;
        this.includeDepartmentsFromPreviousVersion = includeDepartmentsFromPreviousVersion;
    }

    public PublishDocumentRequest() {
    }
}

