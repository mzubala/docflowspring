package pl.com.bottega.docflowjee.docflow.adapters.rest;

public class UpdateDocumentRequest extends DocumentRequest {

    public String title;
    public String content;

    public UpdateDocumentRequest(Long employeeId, Long aggregateVersion, String title, String content) {
        super(employeeId, aggregateVersion);
        this.title = title;
        this.content = content;
    }

    public UpdateDocumentRequest() {
    }
}
