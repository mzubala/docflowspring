package pl.com.bottega.docflowjee.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import pl.com.bottega.docflowjee.catalog.repository.BasicDocumentInfoRepository;
import pl.com.bottega.docflowjee.catalog.repository.DocumentDetailsRepository;
import pl.com.bottega.eventsourcing.Event;

import static org.awaitility.Awaitility.await;
import static pl.com.bottega.docflowjee.catalog.BasicDocumentInfoAssertions.assertBasicDocumentInfo;
import static pl.com.bottega.docflowjee.catalog.DocumentDetailsAssertions.assertDocumentDetails;

@Component
public class EventsSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private BasicDocumentInfoRepository basicDocumentInfoRepository;

    @Autowired
    private DocumentDetailsRepository documentDetailsRepository;

    public void sendEvent(Event event, Long aggregateVersion) {
        jmsTemplate.convertAndSend(String.format("docflow/%s", event.getClass().getSimpleName()), event.withAggregateVersion(aggregateVersion));
        await().untilAsserted(() -> {
            assertBasicDocumentInfo(event.getAggregateId(), basicDocumentInfoRepository).hasAggregateVersion(aggregateVersion);
            assertDocumentDetails(event.getAggregateId(), documentDetailsRepository).hasAggregateVersion(aggregateVersion);
        });
    }

}
