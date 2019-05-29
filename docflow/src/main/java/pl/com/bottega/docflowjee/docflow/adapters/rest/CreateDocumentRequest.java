package pl.com.bottega.docflowjee.docflow.adapters.rest;

public class CreateDocumentRequest {

    public Long empId;

    public CreateDocumentRequest() {
    }

    public CreateDocumentRequest(Long empId) {
        this.empId = empId;
    }
}
