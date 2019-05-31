package pl.com.bottega.docflowjee.catalog.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class BasicDocumentInfo {

    @Id
    private UUID documentId;

    private String title;

    private String contentBrief;

    public Long getAggregateVersion() {
        return aggregateVersion;
    }

    public void setAggregateVersion(Long aggregateVersion) {
        this.aggregateVersion = aggregateVersion;
    }

    private Long aggregateVersion;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getContentBrief() {
        return contentBrief;
    }

    public void setContentBrief(String contentBrief) {
        this.contentBrief = contentBrief;
    }
}
