package pl.com.bottega.docflowjee.catalog.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.docflowjee.catalog.model.DocumentStatus;
import pl.com.bottega.docflowjee.catalog.repository.BasicDocumentInfoRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RestController
@RequestMapping("/catalog")
public class CatalogEndpoint {

    @Autowired
    private BasicDocumentInfoRepository basicDocumentInfoRepository;

    @GetMapping
    public DocumentSearchResults get(DocumentQuery searchParams) {

        var results = basicDocumentInfoRepository.findAll((Specification<BasicDocumentInfoRepository>) (root, query, criteriaBuilder) ->
            buildPredicate(searchParams, root, criteriaBuilder));

        return new DocumentSearchResults().withDocumentDetails(results);
    }

    private Predicate buildPredicate(DocumentQuery searchParams, Root<BasicDocumentInfoRepository> root, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();
        predicate = addTitlePredicate(searchParams.getPhrase(), root, criteriaBuilder, predicate);
        predicate = addStatusPredicate(searchParams.getStatus(), root, criteriaBuilder, predicate);
        return predicate;
    }

    private Predicate addStatusPredicate(DocumentStatus documentStatus, Root<BasicDocumentInfoRepository> root, CriteriaBuilder criteriaBuilder,
                                         Predicate predicate) {
        if (documentStatus != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), documentStatus));
        }
        return predicate;
    }

    private Predicate addTitlePredicate(String phrase, Root<BasicDocumentInfoRepository> root, CriteriaBuilder criteriaBuilder,
                                        Predicate predicate) {
        if (phrase != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%".concat(phrase).concat("%")));
        }
        return predicate;
    }

}