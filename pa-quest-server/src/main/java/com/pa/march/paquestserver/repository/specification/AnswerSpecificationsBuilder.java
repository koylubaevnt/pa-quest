package com.pa.march.paquestserver.repository.specification;

import com.pa.march.paquestserver.domain.Answer;
import com.pa.march.paquestserver.domain.Question;
import com.pa.march.paquestserver.repository.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AnswerSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public AnswerSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public AnswerSpecificationsBuilder with(String key, String operation, Object value, boolean orPredicate) {
        params.add(new SearchCriteria(key, operation, value, orPredicate));
        return this;
    }

    public Specification<Answer> build() {
        if (params.size() == 0) {
            return null;
        }
        Specification<Answer> result = new AnswerSpecification(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = params.get(i-1).isOrPredicate()
                    ? Specification.where(result).or(new AnswerSpecification(params.get(i)))
                    : Specification.where(result).and(new AnswerSpecification(params.get(i)));
        }
        return result;
    }
}
