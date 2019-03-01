package com.pa.march.paquestserver.controller;

import com.pa.march.paquestserver.exception.QuestException;
import com.pa.march.paquestserver.message.resource.AnswerResource;
import com.pa.march.paquestserver.message.resource.QuestionFormResource;
import com.pa.march.paquestserver.message.response.BaseResponse;
import com.pa.march.paquestserver.message.response.DataResponse;
import com.pa.march.paquestserver.message.response.PaddingDataResponse;
import com.pa.march.paquestserver.service.AnswerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/answer")
@PreAuthorize("hasRole('ADMIN')")
public class AnswerController {

    private static final Logger LOG = LoggerFactory.getLogger(AnswerController.class);
    private static final String PAGE = "0";
    private static final String PAGE_SIZE = "10";

    @Autowired
    private AnswerService answerService;

    /**
     * Ответы постранично с фильтрацией
     * @param request
     * @param page      Номер страницы
     * @param pageSize  Количество элементов на странице
     * @param search    Строка поиска
     * @return
     */
    @GetMapping()
    public ResponseEntity<PaddingDataResponse<List<AnswerResource>>> getQuestionPagingFiltering(HttpServletRequest request,
                                                                                                @RequestParam(name="page", defaultValue=PAGE) int page,
                                                                                                @RequestParam(name="page-size", defaultValue=PAGE_SIZE) int pageSize,
                                                                                                @RequestParam(value="search", defaultValue="") String search) {
        LOG.debug("GET: api/answer?page={}&page-size={}&search={}", page, pageSize, search);
        try {
            Page<AnswerResource> resource = answerService.findAnswerPagingFiltering(search, PageRequest.of(page, pageSize));
            PaddingDataResponse<List<AnswerResource>> response = new PaddingDataResponse<>();
            if (resource != null) {
                response.setData(resource.getContent());
                response.setPagingData(resource);
            }
            ResponseEntity<PaddingDataResponse<List<AnswerResource>>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<PaddingDataResponse<List<AnswerResource>>> result = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Добавление ответа
     * @param request
     * @param answerResource  Ответ
     * @return
     */
    @PostMapping()
    ResponseEntity<DataResponse<AnswerResource>> addAnswer(HttpServletRequest request, @RequestBody AnswerResource answerResource) {
        // TODO: добавить валидацию!!! @Validated и обработку на клиенте
        LOG.info("POST: api/answer, RequestBody = {}", answerResource);
        try {
            AnswerResource resource = answerService.addAnswer(answerResource);
            DataResponse<AnswerResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<AnswerResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (QuestException e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<AnswerResource>> result = new ResponseEntity<>(new DataResponse<>(null, e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<AnswerResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Изменение ответа
     * @param request
     * @param answerResource  Ответ
     * @return
     */
    @PutMapping()
    ResponseEntity<DataResponse<AnswerResource>> updateAnswer(HttpServletRequest request, @RequestBody AnswerResource answerResource) {
        LOG.info("PUT api/answer, RequestBody = {}", answerResource);
        try {
            AnswerResource resource = answerService.updateAnswer(answerResource);
            DataResponse<AnswerResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<AnswerResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<AnswerResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Удаление ответа
     * @param request
     * @param answerId    Код ответа
     * @return
     */
    @DeleteMapping("{answerId}")
    ResponseEntity<BaseResponse> deleteAnswer(HttpServletRequest request, @PathVariable Long answerId) {
        LOG.info("DELETE: api/answer/{}", answerId);
        try {
            answerService.deleteAnswer(answerId);
            ResponseEntity<BaseResponse> entity = new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<BaseResponse> result = new ResponseEntity<>(new BaseResponse(500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }


}
