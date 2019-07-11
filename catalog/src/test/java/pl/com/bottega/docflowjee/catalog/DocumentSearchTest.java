package pl.com.bottega.docflowjee.catalog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.docflowjee.catalog.model.DocumentStatus;
import pl.com.bottega.docflowjee.catalog.rest.DocumentQuery;
import pl.com.bottega.docflowjee.catalog.rest.DocumentSearchResults;
import pl.com.bottega.docflowjee.docflow.events.DocumentCreatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentUpdatedEvent;
import pl.com.bottega.eventsourcing.Event;

import java.util.UUID;

import static java.time.Instant.now;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static pl.com.bottega.docflowjee.catalog.DocumentSearchResultsAssertions.assertThatSearchResults;

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

    @Autowired
    private DbCleaner cleaner;

    @Autowired
    private CatalogClient catalogClient;

    @Before
    public void cleanDb() {
        cleaner.cleanDb();
    }

    @Test
    public void searchesDocumentsByPhrase() {
        //given
        eventProcessed(new DocumentCreatedEvent(doc1Id, now(), empId), 0L);
        eventProcessed(new DocumentUpdatedEvent(doc1Id, empId, now(), "testowy tytuł 1", "testowy dokumencik 1", 1), 1L);
        eventProcessed(new DocumentCreatedEvent(doc2Id, now(), empId), 0L);
        eventProcessed(new DocumentUpdatedEvent(doc2Id, empId, now(), "testowy tytuł 2", "testowy dokumencik 2", 1), 1L);
        eventProcessed(new DocumentCreatedEvent(doc3Id, now(), empId), 0L);
        eventProcessed(new DocumentUpdatedEvent(doc3Id, empId, now(), "inny", "inny", 1), 1L);
        eventProcessed(new DocumentCreatedEvent(doc4Id, now(), empId), 0L);
        eventProcessed(new DocumentUpdatedEvent(doc4Id, empId, now(), "całkiem inny tytuł", "całkiem inna treść", 1), 1L);

        // when
        DocumentSearchResults searchResults1 = catalogClient.search(new DocumentQuery().withPhrase("test"));
        DocumentSearchResults searchResults2 = catalogClient.search(new DocumentQuery().withPhrase("brak"));
        DocumentSearchResults searchResults3 = catalogClient.search(new DocumentQuery().withPhrase("test").withStatus(DocumentStatus.PUBLISHED));

        // then
        assertThatSearchResults(searchResults1).hasExactly(doc1Id, doc2Id);
        assertThatSearchResults(searchResults2).hasNoResults();
        assertThatSearchResults(searchResults3).hasNoResults();
    }

    private void eventProcessed(Event event, Long aggregateVersion) {
        sender.sendEvent(event, aggregateVersion);
    }

}
