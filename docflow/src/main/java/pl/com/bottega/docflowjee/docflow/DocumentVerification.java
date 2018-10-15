package pl.com.bottega.docflowjee.docflow;

import pl.com.bottega.docflowjee.docflow.commands.PassToVerificationCommand;
import pl.com.bottega.docflowjee.docflow.commands.RejectDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.VerifyDocumentCommand;

import javax.inject.Inject;
import java.time.Clock;

public class DocumentVerification {

    private final DocumentRepository documentRepository;
    private Clock clock;

    @Inject
    public DocumentVerification(DocumentRepository documentRepository, Clock clock) {
        this.documentRepository = documentRepository;
        this.clock = clock;
    }

    @ValidateCommand
    public void passToVerification(PassToVerificationCommand cmd) {
        Document document = documentRepository.get(cmd.documentId);
        document.passToVerification(cmd);
        documentRepository.save(document, cmd.aggregateVersion);
    }

    @ValidateCommand
    public void verify(VerifyDocumentCommand cmd) {
        Document document = documentRepository.get(cmd.documentId);
        document.verify(cmd);
        documentRepository.save(document, cmd.aggregateVersion);
    }

    @ValidateCommand
    public void reject(RejectDocumentCommand cmd) {
        Document document = documentRepository.get(cmd.documentId);
        document.reject(cmd);
        documentRepository.save(document, cmd.aggregateVersion);
    }

}
