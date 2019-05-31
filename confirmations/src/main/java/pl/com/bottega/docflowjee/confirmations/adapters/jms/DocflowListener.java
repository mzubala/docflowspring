package pl.com.bottega.docflowjee.confirmations.adapters.jms;

import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.com.bottega.docflowjee.confirmations.domain.ConfirmationService;
import pl.com.bottega.docflowjee.docflow.events.DocumentPublishedEvent;

@Component
@AllArgsConstructor
public class DocflowListener {

    private final ConfirmationService confirmationService;

    @JmsListener(destination = "docflow/DocumentPublishedEvent")
    public void handle(DocumentPublishedEvent event) {
        confirmationService.createConfirmations(event).block();
    }

}
