package pl.com.bottega.docflowjee.catalog;

import pl.com.bottega.docflowjee.catalog.model.DocumentStatus;
import pl.com.bottega.docflowjee.catalog.model.DocumentVersion;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentVersionAssertions {
    private DocumentVersion documentVersion;

    public DocumentVersionAssertions(DocumentVersion docVersion) {
        this.documentVersion = docVersion;
    }

    public DocumentVersionAssertions hasStatus(DocumentStatus status) {
        assertThat(documentVersion.getStatus()).isEqualTo(status);
        return this;
    }

    public DocumentVersionAssertions hasTitle(String title) {
        assertThat(documentVersion.getTitle()).isEqualTo(title);
        return this;
    }

    public DocumentVersionAssertions hasContent(String content) {
        assertThat(documentVersion.getContent()).isEqualTo(content);
        return this;
    }

    public DocumentVersionAssertions hasBusinessdocumentVersion(Integer version) {
        assertThat(documentVersion.getDocumentVersionNumber()).isEqualTo(version);
        return this;
    }

    public DocumentVersionAssertions isPublishedFor(Long... employeeIds) {
        assertThat(documentVersion.getPublishedFor()).containsExactly(employeeIds);
        return this;
    }

    public DocumentVersionAssertions hasRejectionReason(String reason) {
        assertThat(documentVersion.getRejectionReason()).isEqualTo(reason);
        return this;
    }
}
