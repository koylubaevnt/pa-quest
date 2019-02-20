package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.message.resource.RoleResource;
import com.pa.march.paquestserver.message.resource.UserResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserService {

    Page<UserResource> findUserPagingFiltering(String search, PageRequest pageable);

    UserResource findUserById(Long id);

    UserResource addUser(UserResource userResource);

    UserResource updateUser(UserResource userResource);

    void deleteUser(Long userId);

    List<RoleResource> findRoles();
}
