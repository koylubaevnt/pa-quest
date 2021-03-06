package com.pa.march.paquestserver.repository;

import com.pa.march.paquestserver.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {

    Collection<Question> findByIdNotIn(Collection<Long> ids);

}
