package com.pa.march.paquestserver.converter;

import com.pa.march.paquestserver.domain.Answer;
import com.pa.march.paquestserver.message.resource.AnswerResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AnswerToResourceConverter extends ConversionServiceAwareConverter<Answer, AnswerResource> {

    @Override
    public AnswerResource convert(Answer answer) {
        log.debug("answer={}", answer);
        AnswerResource answerResource = new AnswerResource();
        answerResource.setId(answer.getId());
        answerResource.setText(answer.getText());

        return answerResource;
    }
}
