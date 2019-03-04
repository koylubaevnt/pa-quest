package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.message.resource.CongratulationResource;

public interface CongratulationService {

    CongratulationResource getCongratulation(UserPrinciple userPrinciple);

    CongratulationResource addCongratulation(CongratulationResource congratulationResource);

    CongratulationResource updateCongratulation(CongratulationResource congratulationResource);

    void deleteCongratulation(Long id);

}
