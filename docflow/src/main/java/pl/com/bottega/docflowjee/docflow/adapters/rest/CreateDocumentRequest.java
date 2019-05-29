package pl.com.bottega.docflowjee.docflow.adapters.rest;

import javax.validation.constraints.NotNull;

public class CreateDocumentRequest {

    @NotNull
    public Long empId;

    public CreateDocumentRequest() {
    }

    public CreateDocumentRequest(Long empId) {
        this.empId = empId;
    }
}
