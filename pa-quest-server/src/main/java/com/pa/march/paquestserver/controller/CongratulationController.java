package com.pa.march.paquestserver.controller;

import com.pa.march.paquestserver.message.request.AnswerForm;
import com.pa.march.paquestserver.message.resource.UserQuestResource;
import com.pa.march.paquestserver.message.response.DataResponse;
import com.pa.march.paquestserver.service.CongratulationService;
import com.pa.march.paquestserver.service.UserPrinciple;
import com.pa.march.paquestserver.service.UserQuestService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/congratulation")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@Slf4j
public class CongratulationController {

    @Autowired
    private CongratulationService congratulationService;

    /**
     * Получить идентификатор видео с YouTube
     *
     * @param request
     * @return              Идентификатор видео на youtube канале
     */
    @GetMapping()
    public ResponseEntity<DataResponse<String>> getQuest(HttpServletRequest request, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        log.info("GET: api/congratulation, userPrinciple={}", userPrinciple);
        try {
            String resource = congratulationService.getYoutubeVideoId(userPrinciple);
            DataResponse<String> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<String>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            log.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<String>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

}
