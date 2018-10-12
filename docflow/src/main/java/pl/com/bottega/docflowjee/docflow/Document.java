package pl.com.bottega.docflowjee.docflow;

import pl.com.bottega.docflowjee.docflow.commands.ArchiveDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateNewDocumentVersionCommand;
import pl.com.bottega.docflowjee.docflow.commands.PassToVerificationCommand;
import pl.com.bottega.docflowjee.docflow.commands.PublishDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.RejectDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.UpdateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.VerifyDocumentCommand;
import pl.com.bottega.docflowjee.docflow.events.DocumentCreatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentUpdatedEvent;
import pl.com.bottega.eventsourcing.AggregateRoot;

import java.time.Clock;

import static pl.com.bottega.docflowjee.docflow.DocumentOperation.UPDATE;
import static pl.com.bottega.docflowjee.docflow.DocumentStatus.DRAFT;

public class Document extends AggregateRoot {

    public Document(CreateDocumentCommand cmd, Clock clock) {
        this.clock = clock;
        applyChange(new DocumentCreatedEvent(cmd.documentId, clock.instant(), cmd.employeeId));
    }
    
    public void update(UpdateDocumentCommand cmd) {
        status.ensureOpPermitted(UPDATE);
        applyChange(new DocumentUpdatedEvent(id, clock.instant(), cmd.title, cmd.content, version));
    }

    public void passToVerification(PassToVerificationCommand cmd) {

    }

    public void verify(VerifyDocumentCommand cmd) {

    }

    public void reject(RejectDocumentCommand cmd) {

    }

    public void publish(PublishDocumentCommand cmd) {

    }

    public void createNewVersion(CreateNewDocumentVersionCommand cmd) {

    }

    public void archive(ArchiveDocumentCommand cmd) {

    }

    private Clock clock;
    private DocumentStatus status;
    private Integer version;

    private void apply(DocumentCreatedEvent event) {
        this.id = event.getAggregateId();
        this.status = DRAFT;
        this.version = 1;
    }

    private void apply(DocumentUpdatedEvent event) {

    }
}
