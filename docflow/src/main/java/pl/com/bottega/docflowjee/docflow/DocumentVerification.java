package pl.com.bottega.docflowjee.docflow;

import pl.com.bottega.docflowjee.docflow.commands.PassToVerificationCommand;
import pl.com.bottega.docflowjee.docflow.commands.RejectDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.VerifyDocumentCommand;

public class DocumentVerification {

    private final DocumentRepository documentRepository;

    public DocumentVerification(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void passToVerification(PassToVerificationCommand cmd) {
        Document document = documentRepository.get(cmd.documentId);
        document.passToVerification(cmd);
        documentRepository.save(document, cmd.aggregateVersion);
    }

    public void verify(VerifyDocumentCommand cmd) {
        Document document = documentRepository.get(cmd.documentId);
        document.verify(cmd);
        documentRepository.save(document, cmd.aggregateVersion);
    }

    public void reject(RejectDocumentCommand cmd) {
        Document document = documentRepository.get(cmd.documentId);
        document.reject(cmd);
        documentRepository.save(document, cmd.aggregateVersion);
    }

}
