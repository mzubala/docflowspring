package pl.com.bottega.docflowjee.docflow;

import pl.com.bottega.docflowjee.docflow.commands.ArchiveDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateNewDocumentVersionCommand;
import pl.com.bottega.docflowjee.docflow.commands.PassToVerificationCommand;
import pl.com.bottega.docflowjee.docflow.commands.PublishDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.RejectDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.UpdateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.VerifyDocumentCommand;
import pl.com.bottega.eventsourcing.AggregateRoot;
import pl.com.bottega.eventsourcing.Event;

import java.time.Clock;

public class Document extends AggregateRoot {

    private Clock clock;

    public Document(CreateDocumentCommand cmd, Clock clock) {
        this.clock = clock;
    }

    @Override
    protected void dispatch(Event event) {

    }

    public void update(UpdateDocumentCommand cmd) {

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
}
