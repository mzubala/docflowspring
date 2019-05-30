package pl.com.bottega.docflowjee.docflow;

import pl.com.bottega.docflowjee.docflow.commands.ArchiveDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateNewDocumentVersionCommand;
import pl.com.bottega.docflowjee.docflow.commands.UpdateDocumentCommand;

import java.time.Clock;

public class DocumentPreparation {
    private final DocumentRepository documentRepository;
    private final EmployeePermissionsPolicy employeePermissionsPolicy;
    private Clock clock;

    public DocumentPreparation(DocumentRepository documentRepository, EmployeePermissionsPolicy employeePermissionsPolicy, Clock clock) {
        this.documentRepository = documentRepository;
        this.employeePermissionsPolicy = employeePermissionsPolicy;
        this.clock = clock;
    }

    public void create(CreateDocumentCommand cmd) {
        if(documentRepository.getOptionally(cmd.getDocumentId()).isPresent()) {
            return;
        }
        Document document = new Document(cmd, clock, employeePermissionsPolicy);
        documentRepository.save(document, -1L);
    }

    public void update(UpdateDocumentCommand cmd) {
        Document document = documentRepository.get(cmd.getDocumentId());
        document.update(cmd);
        documentRepository.save(document, cmd.getAggregateVersion());
    }

    public void createNewVersion(CreateNewDocumentVersionCommand cmd) {
        Document document = documentRepository.get(cmd.getDocumentId());
        document.createNewVersion(cmd);
        documentRepository.save(document, cmd.getAggregateVersion());
    }

    public void archive(ArchiveDocumentCommand cmd) {
        Document document = documentRepository.get(cmd.getDocumentId());
        document.archive(cmd);
        documentRepository.save(document, cmd.getAggregateVersion());
    }
}