package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.message.resource.UserQuestResource;

public interface UserQuestService {

    UserQuestResource findQuestForUser(UserPrinciple userPrinciple);

}
