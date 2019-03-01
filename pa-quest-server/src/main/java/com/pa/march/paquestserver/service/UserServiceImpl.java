package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.domain.Role;
import com.pa.march.paquestserver.domain.RoleName;
import com.pa.march.paquestserver.domain.User;
import com.pa.march.paquestserver.exception.QuestException;
import com.pa.march.paquestserver.message.resource.RoleResource;
import com.pa.march.paquestserver.message.resource.UserResource;
import com.pa.march.paquestserver.message.response.BaseResponse;
import com.pa.march.paquestserver.repository.*;
import com.pa.march.paquestserver.repository.specification.UserSpecificationsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("userService")
@DependsOn(value = { "passwordEncoder" })
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserQuestRepository userQuestRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ConversionService conversionService;

    @Override
    public Page<UserResource> findUserPagingFiltering(String search, PageRequest pageable) {
        Page<User> domains;
        if (search == null || search.isEmpty()) {
            // TODO: разоюраться почему не срабатывает Spring Method Name
            //domains = userRepository.findAllByOrderByUsernameAsc(pageable);
            domains = userRepository.findAll(pageable);
        } else {
            LOG.debug("search = {}", search);
            UserSpecificationsBuilder builder = new UserSpecificationsBuilder();
            Pattern pattern = Pattern.compile(SearchOperation.getPatternString());
            Matcher matcher = pattern.matcher(search);
            while (matcher.find()) {
                LOG.debug("matcher.group = {}", matcher.group(1) + matcher.group(2) + matcher.group(3) + matcher.group(4));
                boolean orPredicate = false;
                if (matcher.group(4).equalsIgnoreCase(SearchOperation.OR)) { orPredicate = true; }
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3), orPredicate);
            }
            Specification<User> spec = builder.build();
            domains = userRepository.findAll(spec, pageable);
        }
        List<User> list = domains.getContent();
        List<UserResource> resource = new ArrayList<>();
        for (User domain : list) {
//            UserResource res = new UserResource();
//            userToUserResource(domain, res);
            UserResource res = conversionService.convert(domain, UserResource.class);
            // TODO: подумать и заменить установку password=null на правильное решение (думаю -> не должны мы слать эти поля просто)
            res.setPassword(null);
            resource.add(res);
        }
        Page<UserResource> res = new PageImpl<>(resource, pageable, domains.getTotalElements());
        return res;
    }

    @Override
    public UserResource findUserById(Long id) {
        BaseResponse response = new BaseResponse();
        try {
            // TODO: Подумать на что можно заменить проверки
            User result = userRepository.findById(id).orElse(null);
            if (result == null) {
                response.setCode(500);
                response.setMessage("Не найден пользователь");
                throw new Exception();
            }

            UserResource userResource = new UserResource();
            userToUserResource(result, userResource);
            // TODO: подумать и заменить установку password=null на праивльное решение (думаю -> не должны мы слать эти поля просто)
            userResource.setPassword(null);
            return userResource;
        } catch (Exception e) {
            if (response.getCode() == 200) {
                response.setCode(500);
                response.setMessage("Неизвестная ошибка");
            }
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new QuestException(response.getCode(), response.getMessage());
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public UserResource addUser(UserResource userResource) {
        BaseResponse response = new BaseResponse();
        try {
            // TODO: Подумать на что можно заменить проверки
            if (userResource.getUsername() == null || userResource.getUsername().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не введен логин пользователя");
                throw new Exception();
            }
            if (userResource.getEmail() == null || userResource.getEmail().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не введена почта пользователя");
                throw new Exception();
            }
            if (userResource.getPassword() == null || userResource.getPassword().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не введен пароль");
                throw new Exception();
            }
            if (userResource.getPasswordConfirm() == null || userResource.getPasswordConfirm().trim().isEmpty()) {
                response.setCode(500);
                response.setMessage("Не введен повторный пароль");
                throw new Exception();
            }
            if (!userResource.getPassword().equals(userResource.getPasswordConfirm())) {
                response.setCode(500);
                response.setMessage("Пароли не совпадают");
                throw new Exception();
            }
            User result = userRepository.findByUsername(userResource.getUsername().trim())
                    .orElse(null);
            if (result != null) {
                response.setCode(500);
                response.setMessage("Пользователь с таким логином уже существует");
                throw new Exception();
            }
            result = userRepository.findByEmail(userResource.getEmail().trim());
            if (result != null) {
                response.setCode(500);
                response.setMessage("Пользователь с таким email уже существует");
                throw new Exception();
            }

            result = new User();
            userResourceToUser(userResource, result);
            result.setId(-1L);
            LOG.debug("addUser(): {}", result.toString());
            result = userRepository.save(result);
            userResource = new UserResource();
            userToUserResource(result, userResource);
            // TODO: подумать и заменить установку password=null на праивльное решение (думаю -> не должны мы слать эти поля просто)
            userResource.setPassword(null);
            return userResource;
        } catch (Exception e) {
            if (response.getCode() == 200) {
                response.setCode(500);
                response.setMessage("Неизвестная ошибка");
            }
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new QuestException(response.getCode(), response.getMessage());
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public UserResource updateUser(UserResource userResource) {
        BaseResponse response = new BaseResponse();
        try {
            // TODO: Подумать на что можно заменить проверки
            User result;
            if(userResource.getEmail() != null) {
                result = userRepository.findByEmail(userResource.getEmail().trim());
                if (result != null && !result.getId().equals(userResource.getId())) {
                    response.setCode(500);
                    response.setMessage("Пользователь с таким email уже существует");
                    throw new Exception();
                }
            }
            result = userRepository.getOne(userResource.getId());
            if (result == null) {
                response.setCode(500);
                response.setMessage("Пользователя не существует");
                throw new Exception();
            }
            if (!result.getUsername().equals(userResource.getUsername())) {
                response.setCode(500);
                response.setMessage("Нельзя менять логин пользователя");
                throw new Exception();
            }
            if (userResource.getPassword() != null && userResource.getPasswordConfirm() != null &&
                    !userResource.getPassword().trim().isEmpty() && !userResource.getPasswordConfirm().trim().isEmpty()) {
                if (!userResource.getPassword().equals(userResource.getPasswordConfirm())) {
                    response.setCode(500);
                    response.setMessage("Пароли не совпадают");
                    throw new Exception();
                }
            }

            userResourceToUser(userResource, result);
            LOG.debug("updateUser(): {}", result.toString());
            result = userRepository.save(result);
            userResource = new UserResource();
            userToUserResource(result, userResource);
            // TODO: подумать и заменить установку password=null на праивльное решение (думаю -> не должны мы слать эти поля просто)
            userResource.setPassword(null);
            return userResource;
        } catch (Exception e) {
            if (response.getCode() == 200) {
                response.setCode(500);
                response.setMessage("Неизвестная ошибка");
            }
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new QuestException(response.getCode(), response.getMessage());
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void deleteUser(Long userId) {
        BaseResponse response = new BaseResponse();
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                Long countUserQuests = userQuestRepository.countByUserId(userId);
                if (countUserQuests > 0) {
                    response.setCode(500);
                    response.setMessage("Есть квесты у данного пользователя");
                    throw new Exception();
                }

                LOG.debug("deleteUser(): id = {}", userId);
                userRepository.deleteById(userId);
            }
        } catch (Exception e) {
            if (response.getCode() == 200) {
                response.setCode(500);
                response.setMessage("Неизвестная ошибка");
            }
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new QuestException(response.getCode(), response.getMessage());
        }
    }

    @Override
    public List<RoleResource> findRoles() {
        List<RoleResource> resource = new ArrayList<>();
        List<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            RoleResource res = new RoleResource();
            roleToRoleResource(role, res);
            resource.add(res);
        }
        return resource;
    }

    private void roleToRoleResource(Role role, RoleResource roleResource) {
        roleResource.setId(role.getId());
        roleResource.setName(role.getName().name().toUpperCase());
    }

    private void userToUserResource(User user, UserResource userResource) {
        BeanUtils.copyProperties(user, userResource);
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name().toUpperCase())
                .collect(Collectors.toSet());
        userResource.setRoles(roles);
    }

    private void userResourceToUser(UserResource userResource, User user) {
        BeanUtils.copyProperties(userResource, user, new String[] { "password" });
        if (userResource.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userResource.getPassword()));
        }
        List<Role> dbRoles = roleRepository.findAll();
        Set<Role> roles = new HashSet<>();
        if (userResource.getRoles() != null) {
            roles = userResource.getRoles().stream()
                    .map(role ->
                            dbRoles.stream()
                                    .filter(e -> e.getName().name().equalsIgnoreCase(role.trim()))
                                    .findFirst()
                                    .orElse(null)
                    )
                    .filter(e -> e != null)
                    .collect(Collectors.toSet());
        }
        if (roles.size() == 0) {
            Role role = roleRepository.findByName(RoleName.ROLE_USER).orElse(null);
            if (role != null) {
                roles.add(role);
            }
        }
        user.setRoles(roles);
    }
}
