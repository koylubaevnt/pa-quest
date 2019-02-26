package com.pa.march.paquestserver.controller;

import com.pa.march.paquestserver.message.resource.UserQuestResource;
import com.pa.march.paquestserver.message.response.DataResponse;
import com.pa.march.paquestserver.service.QuestService;
import com.pa.march.paquestserver.service.UserPrinciple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/userQuest")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class UserQuestController {

    private static final Logger LOG = LoggerFactory.getLogger(UserQuestController.class);
    @Autowired
    private QuestService questService;

    /**
     * Начало пользовательского квеста
     * @param request
     * @param userPrinciple     Пользователь, для которого нужно получить квест
     * @return                  Квест для прохождения
     */
    @GetMapping("/start")
    public ResponseEntity<DataResponse<UserQuestResource>> getQuest(HttpServletRequest request, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        LOG.info("POST: api/userQuest, userPrinciple = {}", userPrinciple);
        try {
            UserQuestResource resource = questService.findQuestForUser(userPrinciple);
            DataResponse<UserQuestResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<UserQuestResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<UserQuestResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

}
