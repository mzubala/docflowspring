package pl.com.bottega.docflowjee.catalog.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.com.bottega.docflowjee.catalog.service.DocflowEventsHandler;
import pl.com.bottega.docflowjee.docflow.events.DocumentArchivedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentCreatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentPassedToVerification;
import pl.com.bottega.docflowjee.docflow.events.DocumentPublishedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentRejectedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentUpdatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentVerifiedEvent;
import pl.com.bottega.docflowjee.docflow.events.NewDocumentVersionCreatedEvent;

@Component
public class DocflowListener  {

    private final DocflowEventsHandler docflowEventsHandler;

    public DocflowListener(DocflowEventsHandler docflowEventsHandler) {
        this.docflowEventsHandler = docflowEventsHandler;
    }

    @JmsListener(destination = "docflow/DocumentCreatedEvent")
    public void listen(DocumentCreatedEvent event) {
        docflowEventsHandler.process(event);
    }

    @JmsListener(destination = "docflow/DocumentUpdatedEvent")
    public void listen(DocumentUpdatedEvent event) {
        docflowEventsHandler.process(event);
    }

    @JmsListener(destination = "docflow/DocumentVerifiedEvent")
    public void listen(DocumentVerifiedEvent event) {
        docflowEventsHandler.process(event);
    }

    @JmsListener(destination = "docflow/DocumentPublishedEvent")
    public void listen(DocumentPublishedEvent event) {
        docflowEventsHandler.process(event);
    }

    @JmsListener(destination = "docflow/DocumentPassedToVerification")
    public void listen(DocumentPassedToVerification event) {
        docflowEventsHandler.process(event);
    }

    @JmsListener(destination = "docflow/DocumentArchivedEvent")
    public void listen(DocumentArchivedEvent event) {
        docflowEventsHandler.process(event);
    }

    @JmsListener(destination = "docflow/NewDocumentVersionCreatedEvent")
    public void listen(NewDocumentVersionCreatedEvent event) {
        docflowEventsHandler.process(event);
    }

    @JmsListener(destination = "docflow/DocumentRejectedEvent")
    public void listen(DocumentRejectedEvent event) {
        docflowEventsHandler.process(event);
    }
}
