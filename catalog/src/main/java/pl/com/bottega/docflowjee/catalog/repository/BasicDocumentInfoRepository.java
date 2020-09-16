package pl.com.bottega.docflowjee.catalog.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import pl.com.bottega.docflowjee.catalog.model.BasicDocumentInfo;

import java.util.UUID;

public interface BasicDocumentInfoRepository extends Repository<BasicDocumentInfo, UUID>, JpaSpecificationExecutor {
    BasicDocumentInfo save(BasicDocumentInfo basicDocumentInfo);

    BasicDocumentInfo findById(UUID aggregateId);
}
