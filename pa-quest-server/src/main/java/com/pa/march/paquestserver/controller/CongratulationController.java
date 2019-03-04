package com.pa.march.paquestserver.controller;

import com.pa.march.paquestserver.domain.Congratulation;
import com.pa.march.paquestserver.message.request.AnswerForm;
import com.pa.march.paquestserver.message.resource.CongratulationResource;
import com.pa.march.paquestserver.message.resource.UserQuestResource;
import com.pa.march.paquestserver.message.response.BaseResponse;
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
@Slf4j
public class CongratulationController {

    @Autowired
    private CongratulationService congratulationService;

    /**
     * Получить поздравление
     *
     * @param request
     * @return              Поздравление
     */
    @GetMapping()
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DataResponse<CongratulationResource>> getCongratulation(HttpServletRequest request, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        log.info("GET: api/congratulation, userPrinciple={}", userPrinciple);
        try {
            CongratulationResource resource = congratulationService.getCongratulation(userPrinciple);
            DataResponse<CongratulationResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<CongratulationResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            log.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<CongratulationResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Добавить поздравление
     *
     * @param request
     * @param congratulationResource
     * @return              Добавленная запись "Поздравления"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<DataResponse<CongratulationResource>> addCongratulation(HttpServletRequest request,  @RequestBody CongratulationResource congratulationResource) {
        log.info("POST: api/congratulation, congratulationResource={}", congratulationResource);
        try {
            CongratulationResource resource = congratulationService.addCongratulation(congratulationResource);
            DataResponse<CongratulationResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<CongratulationResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            log.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<CongratulationResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Изменить поздравление
     *
     * @param request
     * @param congratulationResource
     * @return              Измененная запись "Поздравления"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public ResponseEntity<DataResponse<CongratulationResource>> updateCongratulation(HttpServletRequest request, @RequestBody CongratulationResource congratulationResource) {
        log.info("PUT: api/congratulation, congratulationResource={}", congratulationResource);
        try {
            CongratulationResource resource = congratulationService.updateCongratulation(congratulationResource);
            DataResponse<CongratulationResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<CongratulationResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            log.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<CongratulationResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Удалить поздравление
     *
     * @param request
     * @param id
     * @return              Статус удаления
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<DataResponse<BaseResponse>> deleteCongratulation(HttpServletRequest request, @PathVariable("id") Long id) {
        log.info("DELETE: api/congratulation, id={}", id);
        try {
            congratulationService.deleteCongratulation(id);
            DataResponse<BaseResponse> response = new DataResponse<>();
            ResponseEntity<DataResponse<BaseResponse>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            log.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<BaseResponse>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

}
