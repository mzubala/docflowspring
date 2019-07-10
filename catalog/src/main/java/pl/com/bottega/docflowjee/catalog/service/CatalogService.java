package pl.com.bottega.docflowjee.catalog.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.com.bottega.docflowjee.catalog.model.BasicDocumentInfo;
import pl.com.bottega.docflowjee.catalog.model.DocumentDetails;
import pl.com.bottega.docflowjee.catalog.model.DocumentStatus;
import pl.com.bottega.docflowjee.catalog.model.DocumentVersion;
import pl.com.bottega.docflowjee.catalog.repository.BasicDocumentInfoRepository;
import pl.com.bottega.docflowjee.catalog.repository.CatalogQuery;
import pl.com.bottega.docflowjee.catalog.repository.DocumentDetailsRepository;
import pl.com.bottega.docflowjee.docflow.events.DocumentArchivedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentCreatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentPassedToVerification;
import pl.com.bottega.docflowjee.docflow.events.DocumentPublishedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentRejectedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentUpdatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentVerifiedEvent;
import pl.com.bottega.docflowjee.docflow.events.NewDocumentVersionCreatedEvent;

import javax.transaction.Transactional;
import java.util.UUID;

@Component
public class CatalogService {

    private final BasicDocumentInfoRepository basicDocumentInfoDao;
    private final DocumentDetailsRepository documentDetailsDao;

    public CatalogService(BasicDocumentInfoRepository basicDocumentInfoRepository, DocumentDetailsRepository documentDetailsDao) {
        this.basicDocumentInfoDao = basicDocumentInfoRepository;
        this.documentDetailsDao = documentDetailsDao;
    }

    @Transactional
    public void process(DocumentCreatedEvent event) {

    }

    @Transactional
    public void process(DocumentUpdatedEvent event) {

    }

    private String contentBrief(String content) {
        if(content != null) {
            return content.length() < 250 ? content : content.substring(0, 250);
        } else {
            return null;
        }
    }

    @Transactional
    public void process(DocumentPassedToVerification event) {

    }

    @Transactional
    public void process(DocumentVerifiedEvent event) {

    }

    @Transactional
    public void process(DocumentRejectedEvent event) {

    }

    @Transactional
    public void process(DocumentPublishedEvent event) {

    }

    @Transactional
    public void process(DocumentArchivedEvent event) {

    }

    @Transactional
    public void process(NewDocumentVersionCreatedEvent event) {

    }
}

