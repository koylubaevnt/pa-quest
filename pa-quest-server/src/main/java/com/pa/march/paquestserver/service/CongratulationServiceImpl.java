package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.domain.Congratulation;
import com.pa.march.paquestserver.domain.Role;
import com.pa.march.paquestserver.domain.RoleName;
import com.pa.march.paquestserver.domain.User;
import com.pa.march.paquestserver.exception.QuestException;
import com.pa.march.paquestserver.message.resource.CongratulationResource;
import com.pa.march.paquestserver.message.response.BaseResponse;
import com.pa.march.paquestserver.repository.CongratulationRepository;
import com.pa.march.paquestserver.repository.UserQuestRepository;
import com.pa.march.paquestserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service("congratulationService")
@Slf4j
public class CongratulationServiceImpl implements CongratulationService {

    @Autowired
    private CongratulationRepository congratulationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserQuestRepository userQuestRepository;

    @Autowired
    private ConversionService conversionService;

    @Override
    public CongratulationResource getCongratulation(UserPrinciple userPrinciple) {

        User user = userRepository.findByUsername(userPrinciple.getUsername()).orElse(null);
        if (user == null) {
            return null;
        }
        Role role = user.getRoles().stream()
                .filter(e -> e.getName().equals(RoleName.ROLE_ADMIN))
                .findFirst()
                .orElse(null);
        if (role == null) {
            long count = userQuestRepository.countByUserIdAndActive(userPrinciple.getId(), false);

            if (count == 0) {
                return null;
            }
        }
        Congratulation congratulation = congratulationRepository.findAll().stream()
                .findFirst()
                .orElse(null);

        CongratulationResource congratulationResource = conversionService.convert(congratulation, CongratulationResource.class);

        return congratulationResource;
    }

    @Override
    public CongratulationResource addCongratulation(CongratulationResource congratulationResource) {
        BaseResponse response = new BaseResponse();
        try {
            if (congratulationResource.getVideoId() == null || congratulationResource.getVideoId().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не задан идентификтаор видео для поздравления!");
                throw new Exception();
            }

            Congratulation congratulation = congratulationRepository.findAll().stream()
                    .findFirst()
                    .orElse(null);

            if (congratulation != null) {
                // не будем добавлять, а обновим
                congratulation.setVideoId(congratulationResource.getVideoId());
            } else {
                congratulation = new Congratulation();
            }
            congratulation = congratulationRepository.save(congratulation);

            return conversionService.convert(congratulation, CongratulationResource.class);
        } catch (Exception e) {
            if (response.getCode() == 200) {
                response.setCode(500);
                response.setMessage("Неизвестная ошибка");
            }
            log.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new QuestException(response.getCode(), response.getMessage());
        }
    }

    @Override
    public CongratulationResource updateCongratulation(CongratulationResource congratulationResource) {
        BaseResponse response = new BaseResponse();
        try {
            if (congratulationResource.getVideoId() == null || congratulationResource.getVideoId().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не задан идентификтаор видео для поздравления!");
                throw new Exception();
            }
//            if (congratulationResource.getId() != null || congratulationResource.getId().equals(0)) {
//                response.setCode(500);
//                response.setMessage("Не задан идентификтаор поздравления!");
//                throw new Exception();
//            }
            Congratulation congratulation = congratulationRepository.findAll().stream()
                    .findFirst()
                    .orElse(null);

            if (congratulation != null) {
                // не будем добавлять, а обновим
                congratulation.setVideoId(congratulationResource.getVideoId());
            } else {
                congratulation = new Congratulation();
            }
            congratulation = congratulationRepository.save(congratulation);

            return conversionService.convert(congratulation, CongratulationResource.class);
        } catch (Exception e) {
            if (response.getCode() == 200) {
                response.setCode(500);
                response.setMessage("Неизвестная ошибка");
            }
            log.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new QuestException(response.getCode(), response.getMessage());
        }
    }

    @Override
    public void deleteCongratulation(Long id) {
        BaseResponse response = new BaseResponse();
        response.setCode(500);
        response.setMessage("Операция удаления запрещена.");
        throw new QuestException(response.getCode(), response.getMessage());
    }
}
