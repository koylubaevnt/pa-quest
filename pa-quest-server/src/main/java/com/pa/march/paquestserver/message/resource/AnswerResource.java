package com.pa.march.paquestserver.message.resource;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true, callSuper = true)
public class AnswerResource extends BaseResource {

    private String text;

}
