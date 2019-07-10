package pl.com.bottega.docflowjee.catalog;

import pl.com.bottega.docflowjee.catalog.model.BasicDocumentInfo;
import pl.com.bottega.docflowjee.catalog.model.DocumentStatus;
import pl.com.bottega.docflowjee.catalog.repository.BasicDocumentInfoRepository;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BasicDocumentInfoAssertions {

    private BasicDocumentInfo basicDocumentInfo;

    private BasicDocumentInfoAssertions(BasicDocumentInfo basicDocumentInfo) {
        this.basicDocumentInfo = basicDocumentInfo;
    }

    public static BasicDocumentInfoAssertions assertBasicDocumentInfo(UUID docId, BasicDocumentInfoRepository repository) {
        return new BasicDocumentInfoAssertions(repository.findById(docId));
    }

    public BasicDocumentInfoAssertions isNotNull() {
        assertThat(basicDocumentInfo).isNotNull();
        return this;
    }

    public BasicDocumentInfoAssertions hasStatus(DocumentStatus status) {
        isNotNull();
        assertThat(basicDocumentInfo.getStatus()).isEqualTo(status);
        return this;
    }

    public BasicDocumentInfoAssertions hasAggregateVersion(Long version) {
        isNotNull();
        assertThat(basicDocumentInfo.getAggregateVersion()).isEqualTo(version);
        return this;
    }

    public BasicDocumentInfoAssertions hasTitle(String title) {
        isNotNull();
        assertThat(basicDocumentInfo.getTitle()).isEqualTo(title);
        return this;
    }

    public BasicDocumentInfoAssertions hasContentBrief(String brief) {
        isNotNull();
        assertThat(basicDocumentInfo.getContentBrief()).isEqualTo(brief);
        return this;
    }
}
