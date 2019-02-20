package com.pa.march.paquestserver.repository;

import com.pa.march.paquestserver.domain.Role;
import com.pa.march.paquestserver.domain.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);

}
