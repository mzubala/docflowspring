package pl.com.bottega.docflowjee.catalog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.docflowjee.catalog.model.DocumentStatus;
import pl.com.bottega.docflowjee.catalog.repository.BasicDocumentInfoRepository;
import pl.com.bottega.docflowjee.catalog.repository.DocumentDetailsRepository;
import pl.com.bottega.docflowjee.docflow.events.DocumentArchivedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentCreatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentPassedToVerification;
import pl.com.bottega.docflowjee.docflow.events.DocumentPublishedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentRejectedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentUpdatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentVerifiedEvent;
import pl.com.bottega.docflowjee.docflow.events.NewDocumentVersionCreatedEvent;
import pl.com.bottega.eventsourcing.Event;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static java.time.Instant.now;
import static org.awaitility.Awaitility.await;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static pl.com.bottega.docflowjee.catalog.BasicDocumentInfoAssertions.assertBasicDocumentInfo;
import static pl.com.bottega.docflowjee.catalog.DocumentDetailsAssertions.assertDocumentDetails;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class DocflowEventsHandlerTest {

    @Autowired
    private BasicDocumentInfoRepository basicDocumentInfoRepository;

    @Autowired
    private DocumentDetailsRepository documentDetailsRepository;

    @Autowired
    private EventsSender sender;

    private UUID docId = UUID.randomUUID();

    private Long empId = 1L;

    private Instant now = now();

    private Long aggregateVersion = -1L;

    @Test
    public void handlesDocumentCreatedEvent() {
        // when
        sendEvent(new DocumentCreatedEvent(docId, now, empId));

        // then
        await().untilAsserted(() -> {
            assertBasicDocumentInfo(docId, basicDocumentInfoRepository)
                .hasStatus(DocumentStatus.DRAFT);
            assertDocumentDetails(docId, documentDetailsRepository)
                .hasStatus(DocumentStatus.DRAFT);
        });
    }

    @Test
    public void handlesDocumentUpdatedEvents() {
        // when
        sendEvent(new DocumentCreatedEvent(docId, now(), empId));
        sendEvent(new DocumentUpdatedEvent(docId, empId, now(), "t1", stringOf("1", 500), 1));
        sendEvent(new DocumentUpdatedEvent(docId, empId, now(), "t2", stringOf("2", 1000), 1));

        // then
        await().untilAsserted(() -> {
            assertBasicDocumentInfo(docId, basicDocumentInfoRepository)
                .hasStatus(DocumentStatus.DRAFT)
                .hasTitle("t2")
                .hasContentBrief(stringOf("2", 250));
            assertDocumentDetails(docId, documentDetailsRepository)
                .hasStatus(DocumentStatus.DRAFT)
                .hasTitle("t2")
                .hasContent(stringOf("2", 1000));
        });
    }

    @Test
    public void handlesDocumentPassedToVerification() {
        // when
        sendEvent(new DocumentCreatedEvent(docId, now(), empId));
        sendEvent(new DocumentUpdatedEvent(docId, empId, now(), "t1", stringOf("1", 500), 1));
        sendEvent(new DocumentPassedToVerification(docId, now(), 1));

        // then
        await().untilAsserted(() -> {
            assertBasicDocumentInfo(docId, basicDocumentInfoRepository)
                .hasStatus(DocumentStatus.WAITING_VERIFICATION)
                .hasTitle("t1")
                .hasContentBrief(stringOf("1", 250));
            assertDocumentDetails(docId, documentDetailsRepository)
                .hasStatus(DocumentStatus.WAITING_VERIFICATION)
                .hasTitle("t1")
                .hasContent(stringOf("1", 500));
        });
    }

    @Test
    public void handlesDocumentRejected() {
        // when
        sendEvent(new DocumentCreatedEvent(docId, now(), empId));
        sendEvent(new DocumentUpdatedEvent(docId, empId, now(), "t1", stringOf("1", 500), 1));
        sendEvent(new DocumentPassedToVerification(docId, now(), 1));
        sendEvent(new DocumentRejectedEvent(docId, now(), "reason", 1));

        // then
        await().untilAsserted(() -> {
            assertBasicDocumentInfo(docId, basicDocumentInfoRepository)
                .hasStatus(DocumentStatus.DRAFT)
                .hasTitle("t1")
                .hasContentBrief(stringOf("1", 250));
            assertDocumentDetails(docId, documentDetailsRepository)
                .hasStatus(DocumentStatus.DRAFT)
                .hasTitle("t1")
                .hasContent(stringOf("1", 500))
                .hasRejectionReason("reason");
        });
    }

    @Test
    public void handlesDocumentVerified() {
        // when
        sendEvent(new DocumentCreatedEvent(docId, now(), empId));
        sendEvent(new DocumentUpdatedEvent(docId, empId, now(), "t1", stringOf("1", 500), 1));
        sendEvent(new DocumentPassedToVerification(docId, now(), 1));
        sendEvent(new DocumentVerifiedEvent(docId, now(), 1));

        // then
        await().untilAsserted(() -> {
            assertBasicDocumentInfo(docId, basicDocumentInfoRepository)
                .hasStatus(DocumentStatus.VERIFIED)
                .hasTitle("t1")
                .hasContentBrief(stringOf("1", 250));
            assertDocumentDetails(docId, documentDetailsRepository)
                .hasStatus(DocumentStatus.VERIFIED)
                .hasTitle("t1")
                .hasContent(stringOf("1", 500));
        });
    }

    @Test
    public void handlesDocumentPublished() {
        // when
        sendEvent(new DocumentCreatedEvent(docId, now(), empId));
        sendEvent(new DocumentUpdatedEvent(docId, empId, now(), "t1", stringOf("1", 500), 1));
        sendEvent(new DocumentPassedToVerification(docId, now(), 1));
        sendEvent(new DocumentVerifiedEvent(docId, now(), 1));
        sendEvent(new DocumentPublishedEvent(docId, now(), Set.of(1L, 2L), 1));
        sendEvent(new DocumentPublishedEvent(docId, now(), Set.of(3L, 2L), 1));

        // then
        await().untilAsserted(() -> {
            assertBasicDocumentInfo(docId, basicDocumentInfoRepository)
                .hasStatus(DocumentStatus.PUBLISHED)
                .hasTitle("t1")
                .hasContentBrief(stringOf("1", 250));
            assertDocumentDetails(docId, documentDetailsRepository)
                .hasStatus(DocumentStatus.PUBLISHED)
                .hasTitle("t1")
                .hasContent(stringOf("1", 500))
                .isPublishedFor(1L, 2L, 3L);
        });
    }

    @Test
    public void handlesNewDocumentVersion() {
        // when
        sendEvent(new DocumentCreatedEvent(docId, now(), empId));
        sendEvent(new DocumentUpdatedEvent(docId, empId, now(), "t1", stringOf("1", 500), 1));
        sendEvent(new DocumentPassedToVerification(docId, now(), 1));
        sendEvent(new DocumentVerifiedEvent(docId, now(), 1));
        sendEvent(new DocumentPublishedEvent(docId, now(), Set.of(1L, 2L), 1));
        sendEvent(new NewDocumentVersionCreatedEvent(docId, now(), 2));
        sendEvent(new DocumentUpdatedEvent(docId, empId, now(), "t2", stringOf("2", 1500), 2));

        // then
        await().untilAsserted(() -> {
            assertBasicDocumentInfo(docId, basicDocumentInfoRepository)
                .hasStatus(DocumentStatus.DRAFT)
                .hasTitle("t2")
                .hasContentBrief(stringOf("2", 250));
            assertDocumentDetails(docId, documentDetailsRepository)
                .hasStatus(DocumentStatus.DRAFT)
                .hasTitle("t2")
                .hasContent(stringOf("2", 1500))
                .hasBusinessDocumentVersion(2)
                .hasPreviousVersionThat(1)
                    .hasBusinessdocumentVersion(1)
                    .hasTitle("t1")
                    .hasContent(stringOf("1", 500))
                    .hasStatus(DocumentStatus.PUBLISHED);
        });
    }

    @Test
    public void handlesDocumentArchived() {
        // when
        sendEvent(new DocumentCreatedEvent(docId, now(), empId));
        sendEvent(new DocumentUpdatedEvent(docId, empId, now(), "t1", stringOf("1", 500), 1));
        sendEvent(new DocumentPassedToVerification(docId, now(), 1));
        sendEvent(new DocumentVerifiedEvent(docId, now(), 1));
        sendEvent(new DocumentPublishedEvent(docId, now(), Set.of(1L, 2L), 1));
        sendEvent(new DocumentArchivedEvent(docId, now(), 1));

        // then
        await().untilAsserted(() -> {
            assertBasicDocumentInfo(docId, basicDocumentInfoRepository)
                .hasStatus(DocumentStatus.ARCHIVED)
                .hasTitle("t1")
                .hasContentBrief(stringOf("1", 250));
            assertDocumentDetails(docId, documentDetailsRepository)
                .hasStatus(DocumentStatus.ARCHIVED)
                .hasTitle("t1")
                .hasContent(stringOf("1", 500))
                .isPublishedFor(1L, 2L);
        });
    }

    private void sendEvent(Event event) {
        sender.sendEvent(event, ++aggregateVersion);
    }

    private String stringOf(String string, int count) {
        StringBuilder sb = new StringBuilder();
        while (count-- > 0) {
            sb.append(string);
        }
        return sb.toString();
    }

}
