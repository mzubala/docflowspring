package pl.com.bottega.docflowjee.catalog;

import pl.com.bottega.docflowjee.catalog.model.DocumentDetails;
import pl.com.bottega.docflowjee.catalog.model.DocumentStatus;
import pl.com.bottega.docflowjee.catalog.model.DocumentVersion;
import pl.com.bottega.docflowjee.catalog.repository.DocumentDetailsRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentDetailsAssertions {

    private DocumentDetails documentDetails;

    private DocumentDetailsAssertions(DocumentDetails documentDetails) {
        this.documentDetails = documentDetails;
    }

    public static DocumentDetailsAssertions assertDocumentDetails(UUID docId, DocumentDetailsRepository repository) {
        return new DocumentDetailsAssertions(repository.findByDocumentId(docId));
    }

    public DocumentDetailsAssertions isNotNull() {
        assertThat(documentDetails).isNotNull();
        return this;
    }

    public DocumentDetailsAssertions hasStatus(DocumentStatus status) {
        isNotNull();
        assertThat(documentDetails.getCurrentVersion().getStatus()).isEqualTo(status);
        return this;
    }

    public DocumentDetailsAssertions hasTitle(String title) {
        isNotNull();
        assertThat(documentDetails.getCurrentVersion().getTitle()).isEqualTo(title);
        return this;
    }

    public DocumentDetailsAssertions hasContent(String content) {
        isNotNull();
        assertThat(documentDetails.getCurrentVersion().getContent()).isEqualTo(content);
        return this;
    }

    public DocumentDetailsAssertions hasBusinessDocumentVersion(Integer version) {
        isNotNull();
        assertThat(documentDetails.getCurrentVersion().getDocumentVersionNumber()).isEqualTo(version);
        return this;
    }

    public DocumentDetailsAssertions isPublishedFor(Long... employeeIds) {
        isNotNull();
        assertThat(documentDetails.getCurrentVersion().getPublishedFor()).containsExactly(employeeIds);
        return this;
    }

    public DocumentDetailsAssertions hasRejectionReason(String reason) {
        isNotNull();
        assertThat(documentDetails.getCurrentVersion().getRejectionReason()).isEqualTo(reason);
        return this;
    }

    public DocumentDetailsAssertions hasAggregateVersion(Long version) {
        isNotNull();
        assertThat(documentDetails.getAggregateVersion()).isEqualTo(version);
        return this;
    }

    public DocumentVersionAssertions hasPreviousVersionThat(int version) {
        Optional<DocumentVersion> docVersion = documentDetails.getPreviousVersions().stream().filter(v -> v.getDocumentVersionNumber().equals(version)).findFirst();
        assertThat(docVersion).isNotEmpty();
        return new DocumentVersionAssertions(docVersion.get());
    }
}
