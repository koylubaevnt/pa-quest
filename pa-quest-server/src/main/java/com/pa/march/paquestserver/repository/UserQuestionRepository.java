package com.pa.march.paquestserver.repository;

import com.pa.march.paquestserver.domain.UserQuest;
import com.pa.march.paquestserver.domain.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {


    Long countByQuestionId(Long questionId);

}
