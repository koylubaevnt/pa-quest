package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.message.resource.QuestionFormResource;
import com.pa.march.paquestserver.message.resource.QuestionResource;
import com.pa.march.paquestserver.message.resource.RoleResource;
import com.pa.march.paquestserver.message.resource.UserResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface QuestionService {

    Page<QuestionFormResource> findQuestionPagingFiltering(String search, PageRequest pageable);

    QuestionFormResource addQuestion(QuestionFormResource questionResource);

    QuestionFormResource updateQuestion(QuestionFormResource questionResource);

    void deleteQuestion(Long questionId);

}
