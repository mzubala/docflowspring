package pl.com.bottega.docflowjee.catalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;
import pl.com.bottega.docflowjee.catalog.model.BasicDocumentInfo;

import java.util.UUID;

public interface BasicDocumentInfoRepository extends Repository<BasicDocumentInfo, UUID> {
    BasicDocumentInfo save(BasicDocumentInfo basicDocumentInfo);

    BasicDocumentInfo findById(UUID aggregateId);

    Page<BasicDocumentInfo> findAll(Specification<BasicDocumentInfo> specification, Sort sort, Pageable pageable);
}
