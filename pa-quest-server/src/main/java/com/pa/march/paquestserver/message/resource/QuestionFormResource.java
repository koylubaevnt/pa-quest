package com.pa.march.paquestserver.message.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true, callSuper = true)
public class QuestionFormResource extends BaseResource {

    private String text;

    private AnswerResource correctAnswer;

    private String youtubeVideoId;

    private List<AnswerResource> answers;

}
