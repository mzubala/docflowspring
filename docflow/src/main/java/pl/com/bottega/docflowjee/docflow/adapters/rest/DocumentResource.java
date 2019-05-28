package pl.com.bottega.docflowjee.docflow.adapters.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.docflowjee.docflow.DocumentPreparation;
import pl.com.bottega.docflowjee.docflow.DocumentPublication;
import pl.com.bottega.docflowjee.docflow.DocumentVerification;
import pl.com.bottega.docflowjee.docflow.commands.ArchiveDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateNewDocumentVersionCommand;
import pl.com.bottega.docflowjee.docflow.commands.PassToVerificationCommand;
import pl.com.bottega.docflowjee.docflow.commands.PublishDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.RejectDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.UpdateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.VerifyDocumentCommand;

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

    @PutMapping
    public void update(@PathVariable UUID id,  @RequestBody UpdateDocumentRequest request) {
        var cmd = new UpdateDocumentCommand(id, request.employeeId, request.title, request.content);
        cmd.aggregateVersion = request.aggregateVersion;
        documentPreparation.update(cmd);
    }

    @PostMapping("/verification")
    public void sendToVerification(@PathVariable UUID id,  @RequestBody DocumentRequest request) {
        var cmd = new PassToVerificationCommand(id, request.employeeId);
        cmd.aggregateVersion = request.aggregateVersion;
        documentVerification.passToVerification(cmd);
    }

    @PostMapping("/verification/positive")
    public void verify(@PathVariable UUID id,  @RequestBody DocumentRequest request) {
        var cmd = new VerifyDocumentCommand(id, request.employeeId);
        cmd.aggregateVersion = request.aggregateVersion;
        documentVerification.verify(cmd);
    }

    @PostMapping("/verification/negative")
    public void reject(@PathVariable UUID id,  @RequestBody RejectDocumentRequest request) {
        var cmd = new RejectDocumentCommand(id, request.employeeId, request.reason);
        cmd.aggregateVersion = request.aggregateVersion;
        documentVerification.reject(cmd);
    }

    @PostMapping("/publication")
    public void publish(@PathVariable UUID id,  @RequestBody PublishDocumentRequest request) {
        var cmd = new PublishDocumentCommand(id, request.employeeId, request.departmentIds, true);
        cmd.aggregateVersion = request.aggregateVersion;
        publication.publish(cmd);
    }

    @PostMapping("/new-version")
    public void createNewVersion(@PathVariable UUID id,  @RequestBody DocumentRequest request) {
        var cmd = new CreateNewDocumentVersionCommand(id, request.employeeId);
        cmd.aggregateVersion = request.aggregateVersion;
        documentPreparation.createNewVersion(cmd);
    }

    @PostMapping("/archivisation")
    public void archive(@PathVariable UUID id,  @RequestBody DocumentRequest request) {
        var cmd = new ArchiveDocumentCommand(id, request.employeeId);
        cmd.aggregateVersion = request.aggregateVersion;
        documentPreparation.archive(cmd);
    }
}
