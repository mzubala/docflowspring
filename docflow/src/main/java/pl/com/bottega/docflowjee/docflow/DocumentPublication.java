package pl.com.bottega.docflowjee.docflow;

import pl.com.bottega.docflowjee.docflow.commands.PublishDocumentCommand;

public class DocumentPublication {

    private final DocumentRepository documentRepository;

    public DocumentPublication(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void publish(PublishDocumentCommand cmd) {
        Document document = documentRepository.get(cmd.documentId);
        document.publish(cmd);
        documentRepository.save(document, cmd.aggregateVersion);
    }

}
