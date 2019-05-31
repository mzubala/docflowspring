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
        BasicDocumentInfo basicDocumentInfo = new BasicDocumentInfo();
        basicDocumentInfo.setDocumentId(event.getAggregateId());
        basicDocumentInfo.setStatus(DocumentStatus.DRAFT);

        DocumentDetails details = new DocumentDetails();
        DocumentVersion version = new DocumentVersion();
        version.setDocumentVersionNumber(1);
        version.setStatus(DocumentStatus.DRAFT);
        details.setCurrentVersion(version);
        details.setDocumentId(event.getAggregateId());

        basicDocumentInfo.setAggregateVersion(event.getAggregateVersion());
        details.setAggregateVersion(event.getAggregateVersion());
        basicDocumentInfoDao.save(basicDocumentInfo);
        documentDetailsDao.save(details);
    }

    @Transactional
    public void process(DocumentUpdatedEvent event) {
        BasicDocumentInfo basicDocumentInfo = basicDocumentInfoDao.findById(event.getAggregateId());
        basicDocumentInfo.setStatus(DocumentStatus.DRAFT);
        basicDocumentInfo.setTitle(event.getTitle());
        basicDocumentInfo.setContentBrief(contentBrief(event.getContent()));

        DocumentDetails documentDetails = documentDetailsDao.find(event.getAggregateId());
        DocumentVersion currentVersion = documentDetails.getCurrentVersion();
        currentVersion.setStatus(DocumentStatus.DRAFT);
        currentVersion.setContent(event.getContent());
        currentVersion.setTitle(event.getTitle());

        basicDocumentInfo.setAggregateVersion(event.getAggregateVersion());
        documentDetails.setAggregateVersion(event.getAggregateVersion());
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
        BasicDocumentInfo basicDocumentInfo = basicDocumentInfoDao.findById(event.getAggregateId());
        basicDocumentInfo.setStatus(DocumentStatus.WAITING_VERIFICATION);

        DocumentDetails documentDetails = documentDetailsDao.find(event.getAggregateId());
        DocumentVersion currentVersion = documentDetails.getCurrentVersion();
        currentVersion.setStatus(DocumentStatus.WAITING_VERIFICATION);

        basicDocumentInfo.setAggregateVersion(event.getAggregateVersion());
        documentDetails.setAggregateVersion(event.getAggregateVersion());
    }

    @Transactional
    public void process(DocumentVerifiedEvent event) {
        BasicDocumentInfo basicDocumentInfo = basicDocumentInfoDao.findById(event.getAggregateId());
        basicDocumentInfo.setStatus(DocumentStatus.VERIFIED);

        DocumentDetails documentDetails = documentDetailsDao.find(event.getAggregateId());
        DocumentVersion currentVersion = documentDetails.getCurrentVersion();
        currentVersion.setStatus(DocumentStatus.VERIFIED);

        basicDocumentInfo.setAggregateVersion(event.getAggregateVersion());
        documentDetails.setAggregateVersion(event.getAggregateVersion());
    }

    @Transactional
    public void process(DocumentRejectedEvent event) {
        BasicDocumentInfo basicDocumentInfo = basicDocumentInfoDao.findById(event.getAggregateId());
        basicDocumentInfo.setStatus(DocumentStatus.DRAFT);

        DocumentDetails documentDetails = documentDetailsDao.find(event.getAggregateId());
        DocumentVersion currentVersion = documentDetails.getCurrentVersion();
        currentVersion.setStatus(DocumentStatus.DRAFT);
        currentVersion.setRejectionReason(event.getReason());

        basicDocumentInfo.setAggregateVersion(event.getAggregateVersion());
        documentDetails.setAggregateVersion(event.getAggregateVersion());
    }

    @Transactional
    public void process(DocumentPublishedEvent event) {
        BasicDocumentInfo basicDocumentInfo = basicDocumentInfoDao.findById(event.getAggregateId());
        basicDocumentInfo.setStatus(DocumentStatus.PUBLISHED);

        DocumentDetails documentDetails = documentDetailsDao.find(event.getAggregateId());
        DocumentVersion currentVersion = documentDetails.getCurrentVersion();
        currentVersion.setStatus(DocumentStatus.PUBLISHED);
        currentVersion.getPublishedFor().addAll(event.getDepartmentIds());

        basicDocumentInfo.setAggregateVersion(event.getAggregateVersion());
        documentDetails.setAggregateVersion(event.getAggregateVersion());
    }

    @Transactional
    public void process(DocumentArchivedEvent event) {
        BasicDocumentInfo basicDocumentInfo = basicDocumentInfoDao.findById(event.getAggregateId());
        basicDocumentInfo.setStatus(DocumentStatus.ARCHIVED);

        DocumentDetails documentDetails = documentDetailsDao.find(event.getAggregateId());
        DocumentVersion currentVersion = documentDetails.getCurrentVersion();
        currentVersion.setStatus(DocumentStatus.ARCHIVED);

        basicDocumentInfo.setAggregateVersion(event.getAggregateVersion());
        documentDetails.setAggregateVersion(event.getAggregateVersion());
    }

    @Transactional
    public void process(NewDocumentVersionCreatedEvent event) {
        BasicDocumentInfo basicDocumentInfo = basicDocumentInfoDao.findById(event.getAggregateId());
        basicDocumentInfo.setStatus(DocumentStatus.DRAFT);

        DocumentDetails documentDetails = documentDetailsDao.find(event.getAggregateId());
        DocumentVersion currentVersion = new DocumentVersion();
        currentVersion.setDocumentVersionNumber(event.getVersion());
        currentVersion.setTitle(documentDetails.getCurrentVersion().getTitle());
        currentVersion.setContent(documentDetails.getCurrentVersion().getContent());
        currentVersion.setStatus(DocumentStatus.DRAFT);
        documentDetails.getPreviousVersions().add(documentDetails.getCurrentVersion());
        documentDetails.setCurrentVersion(currentVersion);

        basicDocumentInfo.setAggregateVersion(event.getAggregateVersion());
        documentDetails.setAggregateVersion(event.getAggregateVersion());
    }

    public DocumentDetails getDetails(UUID documentId) {
        return null;
    }

    public Page<BasicDocumentInfo> search(CatalogQuery query) {
        return null;
    }

}

