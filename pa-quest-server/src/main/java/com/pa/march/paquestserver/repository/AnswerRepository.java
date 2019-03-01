package com.pa.march.paquestserver.repository;

import com.pa.march.paquestserver.domain.Answer;
import com.pa.march.paquestserver.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface AnswerRepository extends JpaRepository<Answer, Long>, JpaSpecificationExecutor<Answer> {


    Answer findByTextIgnoreCase(String text);
}
