package pl.com.bottega.docflowjee.catalog.repository;

import org.springframework.data.repository.Repository;
import pl.com.bottega.docflowjee.catalog.model.DocumentDetails;
import java.util.UUID;

public interface DocumentDetailsRepository extends Repository<DocumentDetails, UUID> {

    DocumentDetails save(DocumentDetails details);

    DocumentDetails find(UUID aggregateId);
}
