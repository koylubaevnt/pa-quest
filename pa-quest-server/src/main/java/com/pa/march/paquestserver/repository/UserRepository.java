package com.pa.march.paquestserver.repository;

import com.pa.march.paquestserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);


    //Page<User> findAllByOrderByUsernameAsc(PageRequest pageable);
    //Page<User> findAll(PageRequest pageable);

}
