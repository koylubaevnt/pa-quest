package com.pa.march.paquestserver.repository;

import com.pa.march.paquestserver.domain.UserQuest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuestRepository extends JpaRepository<UserQuest, Long> {

    List<UserQuest> findByUserIdAndActive(Long userId, Boolean active);

    List<UserQuest> findByIdAndUserId(Long userQuestId, Long userId);

    Long countByUserIdAndActive(Long userId, Boolean active);

    Long countByUserId(Long userId);
}
