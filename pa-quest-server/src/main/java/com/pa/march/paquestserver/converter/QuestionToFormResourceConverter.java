package com.pa.march.paquestserver.converter;

import com.pa.march.paquestserver.domain.Answer;
import com.pa.march.paquestserver.domain.Question;
import com.pa.march.paquestserver.message.resource.AnswerResource;
import com.pa.march.paquestserver.message.resource.QuestionFormResource;
import com.pa.march.paquestserver.message.resource.QuestionResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class QuestionToFormResourceConverter extends ConversionServiceAwareConverter<Question, QuestionFormResource> {

    @Override
    public QuestionFormResource convert(Question question) {
        log.debug("question={}", question);
        ConversionService conversionService = conversionService();

        QuestionFormResource questionResource = new QuestionFormResource();
        questionResource.setId(question.getId());
        questionResource.setText(question.getText());
        //questionResource.setContent(conversionService.convert(question.getContent(), ContentResource.class));
        questionResource.setYoutubeVideoId(question.getYoutubeVideoId());

        AnswerResource correctAnswerResource = conversionService.convert(question.getCorrectAnswer(), AnswerResource.class);
        questionResource.setCorrectAnswer(correctAnswerResource);

        TypeDescriptor sourceType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Answer.class));
        TypeDescriptor targetType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(AnswerResource.class));
        questionResource.setAnswers((List<AnswerResource>) conversionService.convert(question.getAnswers(), sourceType, targetType));

        return questionResource;
    }

}
