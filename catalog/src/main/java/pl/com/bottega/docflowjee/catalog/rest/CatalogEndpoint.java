package pl.com.bottega.docflowjee.catalog.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.docflowjee.catalog.model.BasicDocumentInfo;
import pl.com.bottega.docflowjee.catalog.repository.BasicDocumentInfoRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RestController
@RequestMapping("/catalog")
public class CatalogEndpoint {

    @Autowired
    private BasicDocumentInfoRepository repository;

    @GetMapping
    public DocumentSearchResults search(DocumentQuery searchParams) {
         var results = repository.findAll((Specification<BasicDocumentInfo>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            return predicate;
         });
         return new DocumentSearchResults().withDocumentDetails(results);
    }

}
