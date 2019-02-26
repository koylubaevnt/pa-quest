package com.pa.march.paquestserver.message.resource;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true, callSuper = true)
public class UserQuestionResource extends BaseResource {

    private LocalDateTime start;

    private LocalDateTime finish;

    private Integer numberOfAttempts;

    private Boolean answered;

    private QuestionResource question;

}
