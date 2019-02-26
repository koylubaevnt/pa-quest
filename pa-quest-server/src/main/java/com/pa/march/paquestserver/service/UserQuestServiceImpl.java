package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.domain.*;
import com.pa.march.paquestserver.exception.QuestException;
import com.pa.march.paquestserver.helper.TransactionHelper;
import com.pa.march.paquestserver.message.resource.UserQuestResource;
import com.pa.march.paquestserver.repository.UserQuestRepository;
import com.pa.march.paquestserver.repository.QuestionRepository;
import com.pa.march.paquestserver.repository.UserQuestionRepository;
import com.pa.march.paquestserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service("userQuestService")
@Slf4j
public class UserQuestServiceImpl implements UserQuestService {

    private static final int COUNT_QUESTION_IN_QUEST = 5;

    @Autowired
    private UserQuestRepository userQuestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserQuestionRepository userQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private TransactionHelper transactionHelper;

    @Override
    public UserQuestResource findQuestForUser(UserPrinciple userPrinciple) {
        log.debug("userPrinciple={}", userPrinciple);
        Long userId = userPrinciple.getId();
        List<UserQuest> userQuests = userQuestRepository.findByUserIdAndActive(userId, true);
        log.debug("userQuests.size={}", userQuests.size());
        if (userQuests.size() == 0) {
            userQuests = transactionHelper.withTrabsaction(() -> generateQuest(userId));
        }
        UserQuest userQuest = userQuests.get(0);
        log.debug("userQuest={}", userQuest);
        UserQuestResource userQuestResource = conversionService.convert(userQuest, UserQuestResource.class);
        return userQuestResource;
    }

    @Override
    @Transactional
    public UserQuestResource saveAnswer(UserPrinciple userPrinciple, Long userQuestId, Long userQuestionId, Long userAnswerId) {
        log.debug("userPrinciple={}", userPrinciple);
        Long userId = userPrinciple.getId();
        List<UserQuest> userQuests = userQuestRepository.findByIdAndUserId(userQuestId, userId);
        log.debug("userQuests.size={}", userQuests.size());
        if (userQuests.size() == 0) {
            log.error("Пользователь сохраняет ответы не для своего квеста! {}, {}", userId, userQuestId);
            throw new QuestException(500, "Пользователь сохраняет ответы не для своего квеста!");
        }
        UserQuest userQuest = userQuests.get(0);
        UserQuestion userQuestion = userQuest.getUserQuestions().stream()
                .filter(e -> e.getId().equals(userQuestionId))
                .findFirst()
                .orElse(null);
        if (userQuestion == null) {
            log.error("В пользовательском квесте нет такого пользовательского вопроса! {}, {}", userQuestId, userQuestionId);
            throw new QuestException(500, "В пользовательском квесте нет такого пользовательского вопроса!");
        }
        Answer correctAnswer = userQuestion.getQuestion().getCorrectAnswer();
        if (correctAnswer.getId().equals(userAnswerId)) {
            log.debug("Ответ на вопрос корректен");
            LocalDateTime finishTime = LocalDateTime.now();
            userQuestion.setFinish(finishTime);
            userQuestion.setAnswered(true);

            UserQuestion nextUserQuestion = userQuest.getUserQuestions().stream().filter(e -> !e.getAnswered()).findFirst().orElse(null);

            if (nextUserQuestion == null) {
                // Больше нет вопросов для ответов
                log.debug("Больше нет вопросов для ответов");
                userQuest.setActive(false);
                userQuest.setFinish(finishTime);
            } else {
                nextUserQuestion.setStart(LocalDateTime.now());
            }
        } else {
            log.debug("Ответ на вопрос не корректен");
            int count = userQuestion.getNumberOfAttempts() + 1;
            userQuestion.setNumberOfAttempts(count);
        }
        //userQuestionRepository.save(userQuestion);
        userQuest = userQuestRepository.save(userQuest);
        log.debug("userQuest={}", userQuest);
        UserQuestResource userQuestResource = conversionService.convert(userQuest, UserQuestResource.class);
        return userQuestResource;
    }

    @Override
    public Boolean userQuestIsFinished(UserPrinciple userPrinciple) {
        log.debug("userPrinciple={}", userPrinciple);
        Long userId = userPrinciple.getId();
        Long count = userQuestRepository.countByUserIdAndActive(userId, false);
        log.debug("count finished quests: {}", count);
        return count > 0;
    }

    private List<UserQuest> generateQuest(Long userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            log.error("Не смогли найти пользователя с идентификтаором: {} в БД", userId);
            throw new RuntimeException("Пользователь не авторизован. Доступ запрещен"); // Такого не должно быть, н она всякий случай оставим проверку
        }

        long questionCount = questionRepository.count();
        if (questionCount == 0) {
            // Плохо дело, т.к. нет вообще вопросов
            log.error("Нет ни одного вопроса для формирования КВЕСТА");
            throw new RuntimeException("Нет ни одного вопроса для формирования КВЕСТА");
        } else if(questionCount  < COUNT_QUESTION_IN_QUEST) {
            // Плохо дело, т.к. нет нужного количества вопросов
            log.error("Количество вопросов: {} не превышает порог количества вопросов: {} для КВЕСТА", questionCount, COUNT_QUESTION_IN_QUEST);
        }

        List<UserQuest> userQuests = userQuestRepository.findByUserIdAndActive(userId, false);

        Collection<Long> questionIds =
                userQuests.stream()
                        .flatMap(e -> e.getUserQuestions().stream())
                        .map(e -> e.getQuestion().getId())
                .collect(Collectors.toSet());

        final List<Question> list = new ArrayList<>(questionRepository.findByIdNotIn(questionIds));
        boolean notEnoughtQuestion = false;
        if (list.size() == 0) {
            // TODO: подумать как сделать выборку 5 случайных записей из таблицы путем использования запросов к БД.
            // Нет ни одного вопроса, либо уже все вопросы были пройдены пользователем -> Получаем все вопросы, для выборки 5 случайным образом
            log.debug("Нет ни одного вопроса, либо уже все вопросы были пройдены пользователем -> Получаем все вопросы, для выборки 5 случайным образом");
            list.addAll(questionRepository.findAll());
        } else if (list.size() < COUNT_QUESTION_IN_QUEST) {
            // TODO: подумать как сделать выборку требуемого количества случайных записей из таблицы путем использования запросов к БД.
            // не хватает вопросов, для полного квеста -> Тогда добавим сколько надо до "Количества вопросов в квесте"
            notEnoughtQuestion = true;
            final int elementNeed = COUNT_QUESTION_IN_QUEST - list.size();
            log.debug("Не хватает вопросов: {}, для полного квеста -> Тогда добавим сколько надо: {} до \"Количества вопросов в квесте\": {}", list.size(), elementNeed, COUNT_QUESTION_IN_QUEST);
            list.addAll(
                questionRepository.findAllById(questionIds).stream()
                    .limit(elementNeed)
                    .collect(Collectors.toSet())
            );
        }

        List<UserQuestion> userQuestions;
        AtomicInteger index = new AtomicInteger(0);
        final UserQuest userQuest = new UserQuest();
        userQuest.setActive(true);
        userQuest.setStart(LocalDateTime.now());
        userQuest.setUser(user);

        if (notEnoughtQuestion) {
            userQuestions = list.stream()
                    .map( e -> {
                        UserQuestion userQuestion = new UserQuestion();
                        userQuestion.setQuestion(e);
                        userQuestion.setNumberOfAttempts(0);
                        userQuestion.setAnswered(false);
                        userQuestion.setOrder(index.getAndIncrement());
                        userQuestion.setUserQuest(userQuest);
                        return userQuestion;
                    })
                .collect(Collectors.toList());
        } else {
            log.debug("Выбераем случайным образом вопросы для квеста");
            // Выберем случайным образом вопросы для квеста
            Random random = new Random();
            userQuestions = random.ints(COUNT_QUESTION_IN_QUEST, 0, list.size()).
                mapToObj(e -> {
                    log.debug("Случайный индекс: {}", e);
                    UserQuestion userQuestion = new UserQuestion();
                    userQuestion.setQuestion(list.get(e));
                    userQuestion.setNumberOfAttempts(0);
                    userQuestion.setAnswered(false);
                    userQuestion.setOrder(index.getAndIncrement());
                    userQuestion.setUserQuest(userQuest);
                    return userQuestion;
                })
                    .collect(Collectors.toList());
        }
        userQuest.getUserQuestions().addAll(userQuestions);
        userQuest.getUserQuestions().get(0).setStart(LocalDateTime.now());

        UserQuest savedUserQuest = userQuestRepository.save(userQuest);

        return Arrays.asList(savedUserQuest);
    }
}
