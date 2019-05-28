package pl.com.bottega.docflowjee.docflow.adapters.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.docflowjee.docflow.DocumentPreparation;
import pl.com.bottega.docflowjee.docflow.DocumentPublication;
import pl.com.bottega.docflowjee.docflow.DocumentVerification;
import pl.com.bottega.docflowjee.docflow.commands.CreateDocumentCommand;

import java.util.UUID;

@RestController
@RequestMapping("/documents/{id}")
public class DocumentResource {

    private final DocumentPreparation documentPreparation;
    private final DocumentVerification documentVerification;
    private final DocumentPublication publication;

    public DocumentResource(DocumentPreparation documentPreparation, DocumentVerification documentVerification, DocumentPublication publication) {
        this.documentPreparation = documentPreparation;
        this.documentVerification = documentVerification;
        this.publication = publication;
    }

    @PostMapping
    public void create(@PathVariable UUID id,  @RequestBody CreateDocumentRequest request) {
        var cmd = new CreateDocumentCommand(id, request.empId);
        documentPreparation.create(cmd);
    }

}
