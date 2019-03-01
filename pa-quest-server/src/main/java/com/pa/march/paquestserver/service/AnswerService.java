package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.message.resource.AnswerResource;
import com.pa.march.paquestserver.message.resource.QuestionFormResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AnswerService {

    Page<AnswerResource> findAnswerPagingFiltering(String search, PageRequest pageable);

    AnswerResource addAnswer(AnswerResource answerResource);

    AnswerResource updateAnswer(AnswerResource answerResource);

    void deleteAnswer(Long answerId);

}
