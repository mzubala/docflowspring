package pl.com.bottega.docflowjee.docflow;

import pl.com.bottega.docflowjee.docflow.commands.ArchiveDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateNewDocumentVersionCommand;
import pl.com.bottega.docflowjee.docflow.commands.UpdateDocumentCommand;

import java.time.Clock;

public class DocumentPreparation {

    private final DocumentRepository documentRepository;
    private final Clock clock;

    public DocumentPreparation(DocumentRepository documentRepository, Clock clock) {
        this.documentRepository = documentRepository;
        this.clock = clock;
    }

    public void create(CreateDocumentCommand cmd) {
        var document = new Document(cmd, clock);
        documentRepository.save(document, cmd.aggregateVersion);
    }

    public void update(UpdateDocumentCommand cmd) {
        var document = documentRepository.get(cmd.documentId);
        document.update(cmd);
        documentRepository.save(document, cmd.aggregateVersion);
    }

    public void createNewVersion(CreateNewDocumentVersionCommand cmd) {

    }

    public void archive(ArchiveDocumentCommand cmd) {

    }

}
