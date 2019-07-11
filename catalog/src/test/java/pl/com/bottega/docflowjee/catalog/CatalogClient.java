package pl.com.bottega.docflowjee.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.stereotype.Component;
import pl.com.bottega.docflowjee.catalog.model.DocumentDetails;
import pl.com.bottega.docflowjee.catalog.rest.DocumentQuery;
import pl.com.bottega.docflowjee.catalog.rest.DocumentSearchResults;

import java.util.UUID;

@Component
public class CatalogClient {

    @Autowired
    private TestRestTemplate testRestTemplate;

    public DocumentDetails getDetails(UUID docId) {
        return null;
    }

    public DocumentSearchResults search(DocumentQuery query) {
        return null;
    }

}
