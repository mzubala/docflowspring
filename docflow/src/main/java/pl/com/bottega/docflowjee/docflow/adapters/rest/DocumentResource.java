package pl.com.bottega.docflowjee.docflow.adapters.rest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.docflowjee.docflow.DocumentPreparation;
import pl.com.bottega.docflowjee.docflow.DocumentPublication;
import pl.com.bottega.docflowjee.docflow.DocumentVerification;
import pl.com.bottega.docflowjee.docflow.commands.*;

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
    public void create(@PathVariable UUID id,  @RequestBody @Validated CreateDocumentRequest request) {
        var cmd = new CreateDocumentCommand(id, request.empId);
        documentPreparation.create(cmd);
    }

    @PutMapping
    public void update(@PathVariable UUID id,  @RequestBody @Validated UpdateDocumentRequest request) {
        var cmd = new UpdateDocumentCommand(id, request.employeeId, request.title, request.content, request.aggregateVersion);
        documentPreparation.update(cmd);
    }

    @PutMapping("/verification")
    public void sendToVerification(@PathVariable UUID id,  @RequestBody @Validated DocumentRequest request) {
		var cmd = new PassToVerificationCommand(id, request.employeeId, request.aggregateVersion);
		documentVerification.passToVerification(cmd);
    }

    @PutMapping("/verification/positive")
    public void verify(@PathVariable UUID id,  @RequestBody @Validated DocumentRequest request) {
		var cmd = new VerifyDocumentCommand(id, request.employeeId, request.aggregateVersion);
        documentVerification.verify(cmd);
    }

    @PutMapping("/verification/negative")
    public void reject(@PathVariable UUID id,  @RequestBody @Validated RejectDocumentRequest request) {
		var cmd = new RejectDocumentCommand(id, request.employeeId, request.reason, request.aggregateVersion);
        documentVerification.reject(cmd);
    }

    @PutMapping("/publication")
    public void publish(@PathVariable UUID id,  @RequestBody @Validated PublishDocumentRequest request) {
		var cmd = new PublishDocumentCommand(id, request.employeeId, request.departmentIds, true, request.aggregateVersion);
        publication.publish(cmd);
    }

    @PutMapping("/new-version")
    public void createNewVersion(@PathVariable UUID id,  @RequestBody @Validated DocumentRequest request) {
		var cmd = new CreateNewDocumentVersionCommand(id, request.employeeId, request.aggregateVersion);
        documentPreparation.createNewVersion(cmd);
    }

    @PutMapping("/archivisation")
    public void archive(@PathVariable UUID id,  @RequestBody @Validated DocumentRequest request) {
		var cmd = new ArchiveDocumentCommand(id, request.employeeId, request.aggregateVersion);
        documentPreparation.archive(cmd);
    }
}
