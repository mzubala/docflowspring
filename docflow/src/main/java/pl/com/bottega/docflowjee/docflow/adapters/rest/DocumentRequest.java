package pl.com.bottega.docflowjee.docflow.adapters.rest;

public class DocumentRequest {

    public Long employeeId;
    public Long aggregateVersion;

    public DocumentRequest(Long employeeId, Long aggregateVersion) {
        this.employeeId = employeeId;
        this.aggregateVersion = aggregateVersion;
    }

    public DocumentRequest() {
    }
}
