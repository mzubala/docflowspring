package pl.com.bottega.docflowjee.docflow;

import pl.com.bottega.docflowjee.docflow.commands.PublishDocumentCommand;

import javax.inject.Inject;
import java.time.Clock;

public class DocumentPublication {

    private final DocumentRepository documentRepository;
    private Clock clock;

    @Inject
    public DocumentPublication(DocumentRepository documentRepository, Clock clock) {
        this.documentRepository = documentRepository;
        this.clock = clock;
    }

    public void publish(PublishDocumentCommand cmd) {
        Document document = documentRepository.get(cmd.documentId);
        document.publish(cmd);
        documentRepository.save(document, cmd.aggregateVersion);
    }

}
