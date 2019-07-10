package pl.com.bottega.docflowjee.catalog.model;

import java.util.HashSet;
import java.util.Set;

public class DocumentVersion {

    private Long id;

    private Integer documentVersionNumber;

    private String title;

    private String content;

    private DocumentStatus status;

    private Set<Long> publishedFor = new HashSet<>();
    private String rejectionReason;

    public Integer getDocumentVersionNumber() {
        return documentVersionNumber;
    }

    public void setDocumentVersionNumber(Integer documentVersionNumber) {
        this.documentVersionNumber = documentVersionNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public Set<Long> getPublishedFor() {
        return publishedFor;
    }

    public void setPublishedFor(Set<Long> publishedFor) {
        this.publishedFor = publishedFor;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }
}
