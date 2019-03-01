package com.pa.march.paquestserver.repository.specification;

import com.pa.march.paquestserver.domain.Question;
import com.pa.march.paquestserver.repository.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class QuestionSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public QuestionSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public QuestionSpecificationsBuilder with(String key, String operation, Object value, boolean orPredicate) {
        params.add(new SearchCriteria(key, operation, value, orPredicate));
        return this;
    }

    public Specification<Question> build() {
        if (params.size() == 0) {
            return null;
        }
        Specification<Question> result = new QuestionSpecification(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = params.get(i-1).isOrPredicate()
                    ? Specification.where(result).or(new QuestionSpecification(params.get(i)))
                    : Specification.where(result).and(new QuestionSpecification(params.get(i)));
        }
        return result;
    }
}
