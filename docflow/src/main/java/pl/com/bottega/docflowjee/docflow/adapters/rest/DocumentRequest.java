package pl.com.bottega.docflowjee.docflow.adapters.rest;

import javax.validation.constraints.NotNull;

public class DocumentRequest {

    @NotNull
    public Long employeeId;
    @NotNull
    public Long aggregateVersion;

    public DocumentRequest(Long employeeId, Long aggregateVersion) {
        this.employeeId = employeeId;
        this.aggregateVersion = aggregateVersion;
    }

    public DocumentRequest() {
    }
}
