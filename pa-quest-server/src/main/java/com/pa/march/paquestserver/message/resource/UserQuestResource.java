package com.pa.march.paquestserver.message.resource;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true, callSuper = true)
public class UserQuestResource extends BaseResource {

    private Boolean active;

    private List<UserQuestionResource> questions;

}
