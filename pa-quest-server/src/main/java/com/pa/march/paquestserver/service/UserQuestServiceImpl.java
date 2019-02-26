package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.domain.UserQuest;
import com.pa.march.paquestserver.domain.Question;
import com.pa.march.paquestserver.domain.User;
import com.pa.march.paquestserver.domain.UserQuestion;
import com.pa.march.paquestserver.helper.TransactionHelper;
import com.pa.march.paquestserver.message.resource.UserQuestResource;
import com.pa.march.paquestserver.repository.UserQuestRepository;
import com.pa.march.paquestserver.repository.QuestionRepository;
import com.pa.march.paquestserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

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

        final List<Question> list = questionRepository.findByIdNotIn(questionIds).stream()
            .collect(Collectors.toList());
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
                    UserQuestion userQuestion = new UserQuestion();
                    userQuestion.setQuestion(list.get(e));
                    userQuestion.setNumberOfAttempts(0);
                    userQuestion.setOrder(index.getAndIncrement());
                    userQuestion.setUserQuest(userQuest);
                    return userQuestion;
                })
                    .collect(Collectors.toList());
        }
        userQuest.getUserQuestions().addAll(userQuestions);

        UserQuest savedUserQuest = userQuestRepository.save(userQuest);

        return Arrays.asList(savedUserQuest);
    }
}
