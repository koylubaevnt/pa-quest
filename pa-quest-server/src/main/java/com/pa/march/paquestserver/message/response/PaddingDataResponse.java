package com.pa.march.paquestserver.message.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true, includeFieldNames = true, callSuper = true)
public class PaddingDataResponse<T> extends DataResponse {

    private boolean first;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private Sort sort;
    private int numberOfElements;

    public PaddingDataResponse(T data) {
        super(data);
    }

    public PaddingDataResponse(T data, Integer code, String message) {
        super(data, code, message);
    }

    public void setPagingData(Page<?> pagingData) {
        this.first = pagingData.isFirst();
        this.last = pagingData.isLast();
        this.totalPages = pagingData.getTotalPages();
        this.totalElements = pagingData.getTotalElements();
        this.size = pagingData.getSize();
        this.number = pagingData.getNumber();
        this.sort = pagingData.getSort();
        this.numberOfElements = pagingData.getNumberOfElements();
    }

}
