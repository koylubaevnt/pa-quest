package com.pa.march.paquestserver.repository.specification;

import com.pa.march.paquestserver.domain.User;
import com.pa.march.paquestserver.repository.SearchCriteria;
import com.pa.march.paquestserver.repository.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {

    private SearchCriteria criteria;

    public UserSpecification(final SearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    public SearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = null;
        if (criteria.getOperation().equalsIgnoreCase(SearchOperation.GREATER_THAN)) {
            predicate = builder.greaterThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(SearchOperation.LESS_THAN)) {
            predicate = builder.lessThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(SearchOperation.EQUALITY)) {
            predicate = builder.equal(root.get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase(SearchOperation.NEGATION)) {
            predicate = builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase(SearchOperation.LIKE)) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                predicate = builder.like(builder.lower(root.<String>get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
            } else {
                predicate = builder.equal(root.get(criteria.getKey()), criteria.getValue());
                //TODO: https://stackoverflow.com/questions/9802224/jpa-how-to-perform-a-like-with-a-number-column-in-a-static-jpa-metamodel/19516794#19516794
            }
        }
        query.orderBy(builder.asc(root.get(User.FIELD_NAME)));
        return predicate;

    }

}
