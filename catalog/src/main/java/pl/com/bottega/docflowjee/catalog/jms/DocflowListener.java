package pl.com.bottega.docflowjee.catalog.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.com.bottega.docflowjee.catalog.service.CatalogService;
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

    private final CatalogService catalogService;

    public DocflowListener(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @JmsListener(destination = "docflow/DocumentCreatedEvent")
    public void handle(DocumentCreatedEvent event) {
        catalogService.process(event);
    }

    @JmsListener(destination = "docflow/DocumentUpdatedEvent")
    public void handle(DocumentUpdatedEvent event) {
        catalogService.process(event);
    }

    @JmsListener(destination = "docflow/DocumentVerifiedEvent")
    public void handle(DocumentVerifiedEvent event) {
        catalogService.process(event);
    }

    @JmsListener(destination = "docflow/DocumentPublishedEvent")
    public void handle(DocumentPublishedEvent event) {
        catalogService.process(event);
    }

    @JmsListener(destination = "docflow/DocumentPassedToVerification")
    public void handle(DocumentPassedToVerification event) {
        catalogService.process(event);
    }

    @JmsListener(destination = "docflow/DocumentArchivedEvent")
    public void handle(DocumentArchivedEvent event) {
        catalogService.process(event);
    }

    @JmsListener(destination = "docflow/NewDocumentVersionCreatedEvent")
    public void handle(NewDocumentVersionCreatedEvent event) {
        catalogService.process(event);
    }

    @JmsListener(destination = "docflow/DocumentRejectedEvent")
    public void handle(DocumentRejectedEvent event) {
        catalogService.process(event);
    }
}
