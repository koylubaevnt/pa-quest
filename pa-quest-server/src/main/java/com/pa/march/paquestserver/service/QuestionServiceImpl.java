package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.domain.*;
import com.pa.march.paquestserver.exception.QuestException;
import com.pa.march.paquestserver.message.resource.*;
import com.pa.march.paquestserver.message.response.BaseResponse;
import com.pa.march.paquestserver.repository.*;
import com.pa.march.paquestserver.repository.specification.QuestionSpecificationsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private UserQuestionRepository userQuestionRepository;

    @Override
    public Page<QuestionFormResource> findQuestionPagingFiltering(String search, PageRequest pageable) {
        Page<Question> domains;
        if (search == null || search.isEmpty()) {
            domains = questionRepository.findAll(pageable);
        } else {
            LOG.debug("search = {}", search);
            QuestionSpecificationsBuilder builder = new QuestionSpecificationsBuilder();
            Pattern pattern = Pattern.compile(SearchOperation.getPatternString());
            Matcher matcher = pattern.matcher(search);
            while (matcher.find()) {
                LOG.debug("matcher.group = {}", matcher.group(1) + matcher.group(2) + matcher.group(3) + matcher.group(4));
                boolean orPredicate = false;
                if (matcher.group(4).equalsIgnoreCase(SearchOperation.OR)) { orPredicate = true; }
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3), orPredicate);
            }
            Specification<Question> spec = builder.build();
            domains = questionRepository.findAll(spec, pageable);
        }
        List<Question> list = domains.getContent();
        List<QuestionFormResource> resource = new ArrayList<>();
        for (Question domain : list) {
            QuestionFormResource res = conversionService.convert(domain, QuestionFormResource.class);
            resource.add(res);
        }
        Page<QuestionFormResource> res = new PageImpl<>(resource, pageable, domains.getTotalElements());
        return res;
    }

    @Override
    public QuestionFormResource addQuestion(QuestionFormResource questionResource) {
        BaseResponse response = new BaseResponse();
        try {
            if (questionResource.getText() == null || questionResource.getText().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не введен текст вопроса");
                throw new Exception();
            }
            if (questionResource.getYoutubeVideoId() == null || questionResource.getYoutubeVideoId().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не заполнен идентификтаор видео с YouTube");
                throw new Exception();
            }
            // TODO: Добавить проверки, а лучше заменить на @Validated на контроллере
            if (questionResource.getAnswers().size() != 4) {
                response.setCode(500);
                response.setMessage("Ответов в вопросе должно быть 4");
                throw new Exception();
            }
            AnswerResource correctAnswerResource = questionResource.getCorrectAnswer();
            if (correctAnswerResource == null || correctAnswerResource.getId() == null || correctAnswerResource.getText() == null
                || correctAnswerResource.getText().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Правильный ответ не может быть пустым");
                throw new Exception();
            }
            if (questionResource.getAnswers().stream().filter(e-> e.getId() == null || e.getText() == null || e.getText().trim().isEmpty())
                    .findFirst()
                    .orElse(null) != null) {
                response.setCode(500);
                response.setMessage("Ответы не могут быть пустыми в вопросе должно быть 4");
                throw new Exception();
            }
            if (questionResource.getAnswers().stream()
                    .filter(e -> e.getId().equals(correctAnswerResource.getId()))
                    .findFirst()
                    .orElse(null) == null) {
                response.setCode(500);
                response.setMessage("Корректный ответ не присутсвует среди возможных ответов");
                throw new Exception();
            }

            Question question = new Question();
            questionResourceToQuestion(questionResource, question);
            question.setId(-1L);
            LOG.debug("addQuestion(): {}", question.toString());
            question = questionRepository.save(question);

            QuestionFormResource result = conversionService.convert(question, QuestionFormResource.class);
            return result;
        } catch (Exception e) {
            if (response.getCode() == 200) {
                response.setCode(500);
                response.setMessage("Неизвестная ошибка");
            }
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new QuestException(response.getCode(), response.getMessage());
        }
    }

    @Override
    public QuestionFormResource updateQuestion(QuestionFormResource questionResource) {
        BaseResponse response = new BaseResponse();
        try {
            Question question = questionRepository.findById(questionResource.getId()).orElse(null);
            if (question == null){
                response.setCode(500);
                response.setMessage("Нет вопроса с таким идентификатором");
                throw new Exception();
            }
            if (questionResource.getText() == null || questionResource.getText().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не введен текст вопроса");
                throw new Exception();
            }
            if (questionResource.getYoutubeVideoId() == null || questionResource.getYoutubeVideoId().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не заполнен идентификтаор видео с YouTube");
                throw new Exception();
            }
            // TODO: Добавить проверки, а лучше заменить на @Validated на контроллере
            if (questionResource.getAnswers().size() != 4) {
                response.setCode(500);
                response.setMessage("Ответов в вопросе должно быть 4");
                throw new Exception();
            }
            AnswerResource correctAnswerResource = questionResource.getCorrectAnswer();
            if (correctAnswerResource == null || correctAnswerResource.getId() == null || correctAnswerResource.getText() == null
                    || correctAnswerResource.getText().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Правильный ответ не может быть пустым");
                throw new Exception();
            }
            if (questionResource.getAnswers().stream().filter(e-> e.getId() == null || e.getText() == null || e.getText().trim().isEmpty())
                    .findFirst()
                    .orElse(null) != null) {
                response.setCode(500);
                response.setMessage("Ответы не могут быть пустыми в вопросе должно быть 4");
                throw new Exception();
            }
            if (questionResource.getAnswers().stream()
                    .filter(e -> e.getId().equals(correctAnswerResource.getId()))
                    .findFirst()
                    .orElse(null) == null) {
                response.setCode(500);
                response.setMessage("Корректный ответ не присутсвует среди возможных ответов");
                throw new Exception();
            }
            questionResourceToQuestion(questionResource, question);
            LOG.debug("updateQuestion(): {}", question.toString());
            question = questionRepository.save(question);

            QuestionFormResource result = conversionService.convert(question, QuestionFormResource.class);
            return result;
        } catch (Exception e) {
            if (response.getCode() == 200) {
                response.setCode(500);
                response.setMessage("Неизвестная ошибка");
            }
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new QuestException(response.getCode(), response.getMessage());
        }
    }

    @Override
    public void deleteQuestion(Long questionId) {
        BaseResponse response = new BaseResponse();
        try {
            Question question = questionRepository.findById(questionId).orElse(null);
            if (question != null) {
                Long countUserQuestions = userQuestionRepository.countByQuestionId(questionId);
                if (countUserQuestions > 0) {
                    response.setCode(500);
                    response.setMessage("Вопросы уже используются в квестах");
                    throw new Exception();
                }

                LOG.debug("deleteQuestion(): id = {}", questionId);
                questionRepository.deleteById(questionId);
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
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new QuestException(response.getCode(), response.getMessage());
        }
    }

    private void questionResourceToQuestion(QuestionFormResource questionResource, Question question) {
        BeanUtils.copyProperties(questionResource, question);

        if (question.getCorrectAnswer() != null) {
            if (!question.getCorrectAnswer().getId().equals(questionResource.getCorrectAnswer().getId())) {
                Answer correctAnswer = new Answer();
                correctAnswer.setId(questionResource.getCorrectAnswer().getId());
                correctAnswer.setText(questionResource.getCorrectAnswer().getText());
                question.setCorrectAnswer(correctAnswer);
            }
        } else {
            Answer correctAnswer = new Answer();
            correctAnswer.setId(questionResource.getCorrectAnswer().getId());
            correctAnswer.setText(questionResource.getCorrectAnswer().getText());
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
