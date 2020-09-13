package pl.com.bottega.docflowjee.docflow.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.docflowjee.docflow.adapters.rest.CreateDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.DocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.PublishDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.UpdateDocumentRequest;
import pl.com.bottega.docflowjee.docflow.events.DocumentArchivedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentCreatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentPassedToVerification;
import pl.com.bottega.docflowjee.docflow.events.DocumentPublishedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentUpdatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentVerifiedEvent;
import pl.com.bottega.docflowjee.docflow.events.NewDocumentVersionCreatedEvent;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class DocflowTest {

    @Autowired
    private DocflowClient client;

    @Autowired
    private FakeEventPublisher fakeEventPublisher;

    private UUID docId = UUID.randomUUID();
    private Long empId = 1L;
    private Long verifierId = 2L;
    private Long publisherId = 3L;
    private Set<Long> deps = Set.of(1L, 2L, 3L);

    @Before
    public void setup() {
        fakeEventPublisher.reset();
    }

    @Test
    public void supportsBasicDocumentFlow() {
        // when
        long ver = 0;
        client.create(docId, new CreateDocumentRequest(empId));
        client.update(docId, new UpdateDocumentRequest(empId, ver++, "test", "test"));
        client.update(docId, new UpdateDocumentRequest(empId, ver++, "test2", "test2"));
        client.passToVerification(docId, new DocumentRequest(empId, ver++));
        client.verify(docId, new DocumentRequest(verifierId, ver++));
        client.publish(docId, new PublishDocumentRequest(publisherId, ver++, deps, false));

        // then
        fakeEventPublisher.assertEventsWerePublishedInOrder(
            DocumentCreatedEvent.class,
            DocumentUpdatedEvent.class,
            DocumentUpdatedEvent.class,
            DocumentPassedToVerification.class,
            DocumentVerifiedEvent.class,
            DocumentPublishedEvent.class
        );
    }

    @Test
	public void creatingDocumentIsIdempotent() {
		// given
		client.create(docId, new CreateDocumentRequest(empId));
		// when
		client.create(docId, new CreateDocumentRequest(empId));
		// then
		fakeEventPublisher.assertEventsWerePublishedInOrder(
				DocumentCreatedEvent.class
		);
	}

    @Test
    public void supportsArchiving() {
        // when
        long ver = 0;
        client.create(docId, new CreateDocumentRequest(empId));
        client.update(docId, new UpdateDocumentRequest(empId, ver++, "test", "test"));
        client.archive(docId, new DocumentRequest(empId, ver++));

        // then
        fakeEventPublisher.assertEventsWerePublishedInOrder(
            DocumentCreatedEvent.class,
            DocumentUpdatedEvent.class,
            DocumentArchivedEvent.class
        );
    }

    @Test
    public void supportsCreatingNewDocumentVersion() {
        // when
        long ver = 0;
        client.create(docId, new CreateDocumentRequest(empId));
        client.update(docId, new UpdateDocumentRequest(empId, ver++, "test2", "test2"));
        client.passToVerification(docId, new DocumentRequest(empId, ver++));
        client.verify(docId, new DocumentRequest(verifierId, ver++));
        client.publish(docId, new PublishDocumentRequest(publisherId, ver++, deps, false));
        client.createNewVersion(docId, new DocumentRequest(empId, ver));

        // then
        fakeEventPublisher.assertEventWasPublished(
            NewDocumentVersionCreatedEvent.class
        );
    }

}
