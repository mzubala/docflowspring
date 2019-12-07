package pl.com.bottega.docflowjee.catalog.repository;

import org.springframework.data.jpa.domain.Specification;
import pl.com.bottega.docflowjee.catalog.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        root.fetch("address", JoinType.LEFT);
        return criteriaBuilder.conjunction();
    }
}
