package pl.com.bottega.docflowjee.catalog.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@NamedEntityGraph(
    name = "DocumentDetails.all",
    attributeNodes = {
        @NamedAttributeNode(value = "currentVersion", subgraph = "DocumentVersion.all"),
        @NamedAttributeNode(value = "previousVersions", subgraph = "DocumentVersion.all")
    },
    subgraphs = {
        @NamedSubgraph(name = "DocumentVersion.all", attributeNodes = {
            @NamedAttributeNode("publishedFor")
        })
    }
)
public class DocumentDetails {

    @Id
    private UUID documentId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "currentVersionId")
    private DocumentVersion currentVersion;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn(name = "previousVersionsOrder")
    @JoinColumn(name = "documentDetailsId")
    private List<DocumentVersion> previousVersions = new LinkedList<>();

    private Long aggregateVersion;

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public DocumentVersion getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(DocumentVersion currentVersion) {
        this.currentVersion = currentVersion;
    }

    public List<DocumentVersion> getPreviousVersions() {
        return previousVersions;
    }

    public void setPreviousVersions(List<DocumentVersion> previousVersions) {
        this.previousVersions = previousVersions;
    }

    public Long getAggregateVersion() {
        return aggregateVersion;
    }

    public void setAggregateVersion(Long aggregateVersion) {
        this.aggregateVersion = aggregateVersion;
    }
}
