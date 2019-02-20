package com.pa.march.paquestserver.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true)
public class SearchCriteria {

    private String key;
    private String operation;
    private Object value;
    private boolean orPredicate;

}
