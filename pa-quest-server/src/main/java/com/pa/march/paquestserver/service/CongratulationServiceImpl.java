package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.repository.CongratulationRepository;
import com.pa.march.paquestserver.repository.UserQuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("congratulationService")
public class CongratulationServiceImpl implements CongratulationService {

    @Autowired
    private CongratulationRepository congratulationRepository;

    @Autowired
    private UserQuestRepository userQuestRepository;

    @Override
    public String getYoutubeVideoId(UserPrinciple userPrinciple) {

        long count = userQuestRepository.countByUserIdAndActive(userPrinciple.getId(), false);

        return count == 0 ? null : congratulationRepository.findAll().stream()
                .map(e -> e.getVideoId())
                .findFirst()
                .orElse("");
    }

}
