package com.pa.march.paquestserver.converter;

import com.pa.march.paquestserver.domain.UserQuest;
import com.pa.march.paquestserver.domain.UserQuestion;
import com.pa.march.paquestserver.message.resource.UserQuestResource;
import com.pa.march.paquestserver.message.resource.UserQuestionResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserQuestToResourceConverter extends ConversionServiceAwareConverter<UserQuest, UserQuestResource> {

    @Override
    public UserQuestResource convert(UserQuest userQuest) {
        log.debug("userQuest={}", userQuest);
        ConversionService conversionService = conversionService();

        UserQuestResource userQuestResource = new UserQuestResource();
        userQuestResource.setId(userQuest.getId());

        TypeDescriptor sourceType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(UserQuestion.class));
        TypeDescriptor targetType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(UserQuestionResource.class));
        userQuestResource.setQuestions(
                (List<UserQuestionResource>)
                    conversionService.convert(userQuest.getUserQuestions(), sourceType, targetType));
        //userQuestResource.setQuestions();
//        UserResource userResource = null;
//        if (conversionService.canConvert(User.class, UserResource.class)) {
//            userResource = conversionService.convert(userQuest.getUser(), UserResource.class);
//        }
//        userQuestResource.setUser(userResource);

        return userQuestResource;
    }
}
