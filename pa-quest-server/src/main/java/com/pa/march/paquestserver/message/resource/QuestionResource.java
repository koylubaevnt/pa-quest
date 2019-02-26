package com.pa.march.paquestserver.message.resource;

import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true, callSuper = true)
public class QuestionResource extends BaseResource {

    private String text;

    //private ContentResource content;

    private String youtubeVideoId;

    private List<AnswerResource> answers;

}
