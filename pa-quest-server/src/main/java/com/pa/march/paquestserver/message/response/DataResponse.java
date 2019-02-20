package com.pa.march.paquestserver.message.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true, includeFieldNames = true, callSuper = true)
public class DataResponse<T> extends BaseResponse {

    @ToString.Include
    private T data;

    public DataResponse(T data) {
        this();
        this.data = data;
    }

    public DataResponse(T data, Integer code, String message) {
        super(code, message);
        this.data = data;
    }

}
