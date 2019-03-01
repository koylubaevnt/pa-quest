package com.pa.march.paquestserver.repository.specification;

import com.pa.march.paquestserver.domain.User;
import com.pa.march.paquestserver.repository.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public UserSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public UserSpecificationsBuilder with(String key, String operation, Object value, boolean orPredicate) {
        params.add(new SearchCriteria(key, operation, value, orPredicate));
        return this;
    }

    public Specification<User> build() {
        if (params.size() == 0) {
            return null;
        }
        Specification<User> result = new UserSpecification(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = params.get(i-1).isOrPredicate()
                    ? Specification.where(result).or(new UserSpecification(params.get(i)))
                    : Specification.where(result).and(new UserSpecification(params.get(i)));
        }
        return result;
    }

}
