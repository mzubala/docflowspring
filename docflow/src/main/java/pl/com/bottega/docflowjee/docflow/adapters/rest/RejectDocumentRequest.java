package pl.com.bottega.docflowjee.docflow.adapters.rest;

import javax.validation.constraints.NotEmpty;

public class RejectDocumentRequest extends DocumentRequest {

    @NotEmpty
    public String reason;

}
