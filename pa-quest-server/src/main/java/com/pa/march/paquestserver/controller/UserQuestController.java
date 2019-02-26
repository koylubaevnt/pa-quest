package com.pa.march.paquestserver.controller;

import com.pa.march.paquestserver.message.request.AnswerForm;
import com.pa.march.paquestserver.message.resource.UserQuestResource;
import com.pa.march.paquestserver.message.response.DataResponse;
import com.pa.march.paquestserver.service.UserQuestService;
import com.pa.march.paquestserver.service.UserPrinciple;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/quest")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@Slf4j
public class UserQuestController {

    @Autowired
    private UserQuestService userQuestService;

    /**
     * Начало пользовательского квеста
     *
     * @param request
     * @param userPrinciple Пользователь, для которого нужно получить квест
     * @return Квест для прохождения
     */
    @GetMapping("/run")
    public ResponseEntity<DataResponse<UserQuestResource>> getQuest(HttpServletRequest request, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        log.info("GET: api/quest/run, userPrinciple={}", userPrinciple);
        try {
            UserQuestResource resource = userQuestService.findQuestForUser(userPrinciple);
            DataResponse<UserQuestResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<UserQuestResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            log.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<UserQuestResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Сохранить ответ пользователя
     *
     * @param request
     * @param userPrinciple     Пользователь
     * @param answerForm        Ответ пользователя
     * @return                  Квест для прохождения (с изменениями возникшими прри сохранении ответа пользователя)
     */
    @PutMapping("/run")
    public ResponseEntity<DataResponse<UserQuestResource>> saveAnswer(HttpServletRequest request, @AuthenticationPrincipal UserPrinciple userPrinciple,
                                                                      @RequestBody AnswerForm answerForm) {
        log.debug("PUT: api/quest/run, userPrinciple={}, answerForm={}", answerForm);
        try {
            UserQuestResource resource = userQuestService.saveAnswer(userPrinciple, answerForm.getUserQuestId(), answerForm.getUserQuestionId(), answerForm.getUserAnswerId());
            DataResponse<UserQuestResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<UserQuestResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            log.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<UserQuestResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }


    @GetMapping("/finished")
    public ResponseEntity<DataResponse<Boolean>> userQuestIsFinished(HttpServletRequest request, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        log.info("GET: api/quest/finished, userPrinciple={}", userPrinciple);
        try {
            Boolean resource = userQuestService.userQuestIsFinished(userPrinciple);
            DataResponse<Boolean> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<Boolean>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            log.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<Boolean>> result = new ResponseEntity<>(new DataResponse<>(false, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }
}
