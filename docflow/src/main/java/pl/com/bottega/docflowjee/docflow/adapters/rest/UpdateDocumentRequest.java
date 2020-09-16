package pl.com.bottega.docflowjee.docflow.adapters.rest;

import pl.com.bottega.docflowjee.docflow.adapters.rest.validation.ValidateObscenity;

public class UpdateDocumentRequest extends DocumentRequest {

    @ValidateObscenity
    public String title;
    
    @ValidateObscenity
    public String content;

    public UpdateDocumentRequest(Long employeeId, Long aggregateVersion, String title, String content) {
        super(employeeId, aggregateVersion);
        this.title = title;
        this.content = content;
    }

    public UpdateDocumentRequest() {
    }
}
