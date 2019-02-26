package com.pa.march.paquestserver.converter;

import com.pa.march.paquestserver.domain.UserQuestion;
import com.pa.march.paquestserver.message.resource.QuestionResource;
import com.pa.march.paquestserver.message.resource.UserQuestionResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserQuestionToResourceConverter extends ConversionServiceAwareConverter<UserQuestion, UserQuestionResource> {

    @Override
    public UserQuestionResource convert(UserQuestion userQuestion) {
        log.debug("userQuestion={}", userQuestion);
        UserQuestionResource userQuestionResource = new UserQuestionResource();
        userQuestionResource.setId(userQuestion.getId());
        userQuestionResource.setStart(userQuestion.getStart());
        userQuestionResource.setFinish(userQuestion.getFinish());
        userQuestionResource.setNumberOfAttempts(userQuestion.getNumberOfAttempts());

        userQuestionResource.setQuestion(conversionService().convert(userQuestion.getQuestion(), QuestionResource.class));

        return userQuestionResource;
    }

}
