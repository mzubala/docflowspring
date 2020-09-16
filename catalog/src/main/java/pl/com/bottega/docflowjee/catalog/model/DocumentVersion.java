package pl.com.bottega.docflowjee.catalog.model;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
public class DocumentVersion {

    @Id
    @GeneratedValue
    private Long id;

    private Integer documentVersionNumber;

    private String title;

    @Column(length = 1 << 16)
    private String content;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @ElementCollection
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
