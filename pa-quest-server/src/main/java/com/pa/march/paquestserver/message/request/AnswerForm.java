package com.pa.march.paquestserver.message.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AnswerForm {

    @NotBlank
    @NotNull
    private Long userQuestId;

    @NotBlank
    @NotNull
    private Long userQuestionId;

    @NotBlank
    @NotNull
    private Long userAnswerId;

}
