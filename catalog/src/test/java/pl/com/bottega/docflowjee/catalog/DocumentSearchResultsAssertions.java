package pl.com.bottega.docflowjee.catalog;

import pl.com.bottega.docflowjee.catalog.rest.DocumentSearchResults;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentSearchResultsAssertions {

    private DocumentSearchResults searchResults;

    private DocumentSearchResultsAssertions(DocumentSearchResults searchResults) {
        this.searchResults = searchResults;
    }

    public static DocumentSearchResultsAssertions assertThatSearchResults(DocumentSearchResults searchResults) {
        return new DocumentSearchResultsAssertions(searchResults);
    }

    public DocumentSearchResultsAssertions hasExactly(UUID... docIds) {
        assertThat(searchResults.getDocumentDetails().stream().map(d -> d.getDocumentId())).containsExactly(docIds);
        return this;
    }

    public DocumentSearchResultsAssertions hasNoResults() {
        assertThat(searchResults.getDocumentDetails()).isEmpty();
        return this;
    }
}
