package com.pa.march.paquestserver.controller;

import com.pa.march.paquestserver.exception.QuestException;
import com.pa.march.paquestserver.message.resource.QuestionFormResource;
import com.pa.march.paquestserver.message.response.BaseResponse;
import com.pa.march.paquestserver.message.response.DataResponse;
import com.pa.march.paquestserver.message.response.PaddingDataResponse;
import com.pa.march.paquestserver.service.QuestionService;
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
@RequestMapping("/api/question")
@PreAuthorize("hasRole('ADMIN')")
public class QuestionController {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);
    private static final String PAGE = "0";
    private static final String PAGE_SIZE = "10";

    @Autowired
    private QuestionService questionService;

    /**
     * Вопросы постранично с фильтрацией
     * @param request
     * @param page      Номер страницы
     * @param pageSize  Количество элементов на странице
     * @param search    Строка поиска
     * @return
     */
    @GetMapping()
    public ResponseEntity<PaddingDataResponse<List<QuestionFormResource>>> getQuestionPagingFiltering(HttpServletRequest request,
                                                                                                  @RequestParam(name="page", defaultValue=PAGE) int page,
                                                                                                  @RequestParam(name="page-size", defaultValue=PAGE_SIZE) int pageSize,
                                                                                                  @RequestParam(value="search", defaultValue="") String search) {
        LOG.debug("GET: api/question?page={}&page-size={}&search={}", page, pageSize, search);
        try {
            Page<QuestionFormResource> resource = questionService.findQuestionPagingFiltering(search, PageRequest.of(page, pageSize));
            PaddingDataResponse<List<QuestionFormResource>> response = new PaddingDataResponse<>();
            if (resource != null) {
                response.setData(resource.getContent());
                response.setPagingData(resource);
            }
            ResponseEntity<PaddingDataResponse<List<QuestionFormResource>>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<PaddingDataResponse<List<QuestionFormResource>>> result = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Добавление вопроса
     * @param request
     * @param questionResource  Вопрос
     * @return
     */
    @PostMapping()
    ResponseEntity<DataResponse<QuestionFormResource>> addQuestion(HttpServletRequest request, @RequestBody QuestionFormResource questionResource) {
        // TODO: добавить валидацию!!! @Validated и обработку на клиенте
        LOG.info("POST: api/question, RequestBody = {}", questionResource);
        try {
            QuestionFormResource resource = questionService.addQuestion(questionResource);
            DataResponse<QuestionFormResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<QuestionFormResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (QuestException e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<QuestionFormResource>> result = new ResponseEntity<>(new DataResponse<>(null, e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<QuestionFormResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Изменение вопроса
     * @param request
     * @param questionResource  Вопрос
     * @return
     */
    @PutMapping()
    ResponseEntity<DataResponse<QuestionFormResource>> updateQuestion(HttpServletRequest request, @RequestBody QuestionFormResource questionResource) {
        LOG.info("PUT api/question, RequestBody = {}", questionResource);
        try {
            QuestionFormResource resource = questionService.updateQuestion(questionResource);
            DataResponse<QuestionFormResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<QuestionFormResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<QuestionFormResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Удаление вопроса
     * @param request
     * @param questionId    Код вопроса
     * @return
     */
    @DeleteMapping("{questionId}")
    ResponseEntity<BaseResponse> deleteQuestion(HttpServletRequest request, @PathVariable Long questionId) {
        LOG.info("DELETE: api/question/{}", questionId);
        try {
            questionService.deleteQuestion(questionId);
            ResponseEntity<BaseResponse> entity = new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<BaseResponse> result = new ResponseEntity<>(new BaseResponse(500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }


}
