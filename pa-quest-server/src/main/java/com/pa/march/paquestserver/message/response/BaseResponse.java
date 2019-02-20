package com.pa.march.paquestserver.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true)
public class BaseResponse {

    @JsonProperty(value = "response_code", required = true)
    private Integer code = 200;

    @JsonProperty(value = "response_message", required = true)
    private String message = "Операция выполнена успешно";

    @Override
    public String toString() {
        return "ResponseResource [code=" + code + ", message=" + message + "]";
    }

}
