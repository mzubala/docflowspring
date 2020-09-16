
package pl.com.bottega.docflowjee.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.stereotype.Component;
import pl.com.bottega.docflowjee.catalog.model.DocumentDetails;
import pl.com.bottega.docflowjee.catalog.rest.DocumentQuery;
import pl.com.bottega.docflowjee.catalog.rest.DocumentSearchResults;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class CatalogClient {

    @Autowired
    private TestRestTemplate testRestTemplate;

    public DocumentDetails getDetails(UUID docId) {
        return null;
    }

    public DocumentSearchResults search(DocumentQuery query) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("status", query.getStatus());
        queryMap.put("phrase", query.getPhrase());
        queryMap.put("page", query.getPage());
        queryMap.put("perPage", query.getPerPage());

        return testRestTemplate.
            getForEntity("/catalog?status={status}&phrase={phrase}", DocumentSearchResults.class, queryMap).
            getBody();
    }

}