package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.domain.Answer;
import com.pa.march.paquestserver.domain.Question;
import com.pa.march.paquestserver.exception.QuestException;
import com.pa.march.paquestserver.message.resource.AnswerResource;
import com.pa.march.paquestserver.message.resource.QuestionFormResource;
import com.pa.march.paquestserver.message.response.BaseResponse;
import com.pa.march.paquestserver.repository.AnswerRepository;
import com.pa.march.paquestserver.repository.QuestionRepository;
import com.pa.march.paquestserver.repository.SearchOperation;
import com.pa.march.paquestserver.repository.specification.AnswerSpecificationsBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("answerService")
@Slf4j
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public Page<AnswerResource> findAnswerPagingFiltering(String search, PageRequest pageable) {
        Page<Answer> domains;
        if (search == null || search.isEmpty()) {
            domains = answerRepository.findAll(pageable);
        } else {
            log.debug("search = {}", search);
            AnswerSpecificationsBuilder builder = new AnswerSpecificationsBuilder();
            Pattern pattern = Pattern.compile(SearchOperation.getPatternString());
            Matcher matcher = pattern.matcher(search);
            while (matcher.find()) {
                log.debug("matcher.group = {}", matcher.group(1) + matcher.group(2) + matcher.group(3) + matcher.group(4));
                boolean orPredicate = false;
                if (matcher.group(4).equalsIgnoreCase(SearchOperation.OR)) { orPredicate = true; }
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3), orPredicate);
            }
            Specification<Answer> spec = builder.build();
            domains = answerRepository.findAll(spec, pageable);
        }
        List<Answer> list = domains.getContent();
        List<AnswerResource> resource = new ArrayList<>();
        for (Answer domain : list) {
            AnswerResource res = conversionService.convert(domain, AnswerResource.class);
            resource.add(res);
        }
        Page<AnswerResource> res = new PageImpl<>(resource, pageable, domains.getTotalElements());
        return res;
    }

    @Override
    public AnswerResource addAnswer(AnswerResource answerResource) {
        BaseResponse response = new BaseResponse();
        try {
            if (answerResource.getText() == null || answerResource.getText().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не введен текст ответа");
                throw new Exception();
            }
            Answer otherAnswer = answerRepository.findByTextIgnoreCase(answerResource.getText().trim());
            if (otherAnswer != null) {
                response.setCode(500);
                response.setMessage("Такой текст вопроса уже есть");
                throw new Exception();
            }
            Answer answer = new Answer();
            answer.setText(answerResource.getText().trim());
            log.debug("addAnswer(): {}", answer.toString());
            answer = answerRepository.save(answer);

            AnswerResource result = conversionService.convert(answer, AnswerResource.class);
            return result;
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
    public AnswerResource updateAnswer(AnswerResource answerResource) {
        BaseResponse response = new BaseResponse();
        try {
            Answer answer = answerRepository.findById(answerResource.getId()).orElse(null);
            if (answer == null) {
                response.setCode(500);
                response.setMessage("Не найден ответ с таким идентификатором");
                throw new Exception();
            }
            if (answerResource.getText() == null || answerResource.getText().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не введен текст ответа");
                throw new Exception();
            }
            if (!answer.getText().trim().equals(answerResource.getText().trim())) {
                Answer otherAnswer = answerRepository.findByTextIgnoreCase(answerResource.getText().trim());
                if (otherAnswer != null && !otherAnswer.getId().equals(answer.getId())) {
                    response.setCode(500);
                    response.setMessage("Такой текст вопроса уже есть");
                    throw new Exception();
                } else {
                    answer.setText(answerResource.getText().trim());
                    log.debug("updateAnswer(): {}", answer.toString());
                    answer = answerRepository.save(answer);
                }
            }
            AnswerResource result = conversionService.convert(answer, AnswerResource.class);
            return result;
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
    public void deleteAnswer(Long answerId) {
        BaseResponse response = new BaseResponse();
        try {
            Answer answer = answerRepository.findById(answerId).orElse(null);
            if (answer != null) {
                //Long countUserQuestions = questionRepository..coountByQuestionId(answerId);
//                if (countUserQuestions > 0) {
//                    response.setCode(500);
//                    response.setMessage("Вопросы уже используются в квестах");
//                    throw new Exception();
//                }

                log.debug("deleteAnswer(): id = {}", answerId);
                answerRepository.deleteById(answerId);
            } else {
                response.setCode(500);
                response.setMessage("Вопроса с таким идентификатором не существует");
                throw new QuestException(response.getCode(), response.getMessage());
            }
        } catch (Exception e) {
            if (response.getCode() == 200) {
                response.setCode(500);
                response.setMessage("Неизвестная ошибка");
            }
            log.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new QuestException(response.getCode(), response.getMessage());
        }
    }

    private void questionResourceToQuestion(QuestionFormResource questionResource, Question question) {
        BeanUtils.copyProperties(question, questionResource);

        if (question.getCorrectAnswer() != null) {
            if (!question.getCorrectAnswer().getId().equals(questionResource.getCorrectAnswer().getId())) {
                Answer correctAnswer = new Answer();
                correctAnswer.setId(questionResource.getId());
                correctAnswer.setText(questionResource.getText());
                question.setCorrectAnswer(correctAnswer);
            }
        } else {
            Answer correctAnswer = new Answer();
            correctAnswer.setId(questionResource.getId());
            correctAnswer.setText(questionResource.getText());
            question.setCorrectAnswer(correctAnswer);
        }

//        if (question.getAnswers() != null && question.getAnswers().size() > 0) {
//
//        } else {
            List<Answer> answers = new ArrayList<>();
            for (AnswerResource answerResource : questionResource.getAnswers()) {
                Answer answer = new Answer();
                answer.setId(answerResource.getId());
                answer.setText(answerResource.getText());
                answers.add(answer);
            }
            question.setAnswers(answers);
//        }
    }

}
