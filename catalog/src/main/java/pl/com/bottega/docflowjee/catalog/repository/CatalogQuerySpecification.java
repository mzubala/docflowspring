package pl.com.bottega.docflowjee.catalog.repository;

import org.springframework.data.jpa.domain.Specification;
import pl.com.bottega.docflowjee.catalog.model.BasicDocumentInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CatalogQuerySpecification implements Specification<BasicDocumentInfo> {

    private final CatalogQuery query;

    public CatalogQuerySpecification(CatalogQuery query) {
        this.query = query;
    }

    @Override
    public Predicate toPredicate(Root<BasicDocumentInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();
        if(query.phrase != null) {
            var phrase = "%" + query.phrase + "%";
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(
                criteriaBuilder.like(root.get("title"), phrase),
                criteriaBuilder.like(root.get("contentBrief"), phrase)
            ));
        }
        if(query.status != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), query.status));
        }
        return predicate;
    }
}
