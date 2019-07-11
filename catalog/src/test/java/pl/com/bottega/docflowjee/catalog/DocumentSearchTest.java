package pl.com.bottega.docflowjee.catalog;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.docflowjee.docflow.events.DocumentCreatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentUpdatedEvent;
import pl.com.bottega.eventsourcing.Event;

import java.util.UUID;

import static java.time.Instant.now;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class DocumentSearchTest {

    private UUID doc1Id = UUID.randomUUID();
    private UUID doc2Id = UUID.randomUUID();
    private UUID doc3Id = UUID.randomUUID();
    private UUID doc4Id = UUID.randomUUID();

    private Long empId = 1L;

    @Autowired
    private EventsSender sender;

    public void searchesDocumentsByPhrase() {
        //when
        sendEvent(new DocumentCreatedEvent(doc1Id, now(), empId), 0L);
        sendEvent(new DocumentUpdatedEvent(doc1Id, empId, now(), "testowy tytuł 2", "testowy dokumencik 1", 1), 1L);
        sendEvent(new DocumentCreatedEvent(doc2Id, now(), empId), 0L);
        sendEvent(new DocumentUpdatedEvent(doc2Id, empId, now(), "testowy tytuł 2", "testowy dokumencik 2", 1), 1L);
        sendEvent(new DocumentCreatedEvent(doc3Id, now(), empId), 0L);
        sendEvent(new DocumentUpdatedEvent(doc3Id, empId, now(), "inny", "inny", 1), 1L);
    }

    private void sendEvent(Event event, Long aggregateVersion) {
        sender.sendEvent(event, aggregateVersion);
    }

}
