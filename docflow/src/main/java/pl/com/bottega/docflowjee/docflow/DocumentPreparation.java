package pl.com.bottega.docflowjee.docflow;

import pl.com.bottega.docflowjee.docflow.commands.ArchiveDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateNewDocumentVersionCommand;
import pl.com.bottega.docflowjee.docflow.commands.UpdateDocumentCommand;

import java.time.Clock;

public class DocumentPreparation {
    private final DocumentRepository documentRepository;
    private Clock clock;

    public DocumentPreparation(DocumentRepository documentRepository, Clock clock) {
        this.documentRepository = documentRepository;
        this.clock = clock;
    }

    public void create(CreateDocumentCommand cmd) {
        if(documentRepository.getOptionally(cmd.getDocumentId()).isPresent()) {
            return;
        }
        Document document = new Document(cmd, clock);
        documentRepository.save(document, -1L);
    }

    @ValidateCommand
    public void update(UpdateDocumentCommand cmd) {
        Document document = documentRepository.get(cmd.getDocumentId());
        document.update(cmd);
        documentRepository.save(document, cmd.getAggregateVersion());
    }

    @ValidateCommand
    public void createNewVersion(CreateNewDocumentVersionCommand cmd) {
        Document document = documentRepository.get(cmd.getDocumentId());
        document.createNewVersion(cmd);
        documentRepository.save(document, cmd.getAggregateVersion());
    }

    @ValidateCommand
    public void archive(ArchiveDocumentCommand cmd) {
        Document document = documentRepository.get(cmd.getDocumentId());
        document.archive(cmd);
        documentRepository.save(document, cmd.getAggregateVersion());
    }
}