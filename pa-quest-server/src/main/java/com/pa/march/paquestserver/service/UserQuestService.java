package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.message.resource.UserQuestResource;

public interface UserQuestService {

    UserQuestResource findQuestForUser(UserPrinciple userPrinciple);

    UserQuestResource saveAnswer(UserPrinciple userPrinciple, Long userQuestId, Long userQuestionId, Long userAnswerId);

    Boolean userQuestIsFinished(UserPrinciple userPrinciple);
}
