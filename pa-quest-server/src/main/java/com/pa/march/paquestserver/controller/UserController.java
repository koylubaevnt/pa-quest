package com.pa.march.paquestserver.controller;

import com.pa.march.paquestserver.exception.QuestException;
import com.pa.march.paquestserver.message.resource.RoleResource;
import com.pa.march.paquestserver.message.resource.UserResource;
import com.pa.march.paquestserver.message.response.BaseResponse;
import com.pa.march.paquestserver.message.response.DataResponse;
import com.pa.march.paquestserver.message.response.PaddingDataResponse;
import com.pa.march.paquestserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private static final String PAGE = "0";
    private static final String PAGE_SIZE = "10";

    @Autowired
    private UserService userService;

    /**
     * Пользователи постранично с фильтрацией
     * @param request
     * @param page      Номер страницы
     * @param pageSize  Количество элементов на странице
     * @param search    Строка поиска
     * @return
     */
    @GetMapping()
    public ResponseEntity<PaddingDataResponse<List<UserResource>>> getUserPagingFiltering(HttpServletRequest request,
                                                                                   @RequestParam(name="page", defaultValue=PAGE) int page,
                                                                                   @RequestParam(name="page-size", defaultValue=PAGE_SIZE) int pageSize,
                                                                                   @RequestParam(value="search", defaultValue="") String search) {
        LOG.debug("GET: api/user?page={}&page-size={}&search={}", page, pageSize, search);
        try {
            Page<UserResource> resource = userService.findUserPagingFiltering(search, PageRequest.of(page, pageSize));
            PaddingDataResponse<List<UserResource>> response = new PaddingDataResponse<>();
            if (resource != null) {
                response.setData(resource.getContent());
                response.setPagingData(resource);
            }
            ResponseEntity<PaddingDataResponse<List<UserResource>>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<PaddingDataResponse<List<UserResource>>> result = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Добавление пользователя
     * @param request
     * @param userResource  Пользователь
     * @return
     */
    @PostMapping()
    ResponseEntity<DataResponse<UserResource>> addUser(HttpServletRequest request, @RequestBody UserResource userResource) {
        // TODO: добавить валидацию!!! @Validated и обработку на клиенте
        LOG.info("POST: api/user, RequestBody = {}", userResource);
        try {
            UserResource resource = userService.addUser(userResource);
            DataResponse<UserResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<UserResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (QuestException e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<UserResource>> result = new ResponseEntity<>(new DataResponse<>(null, e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<UserResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Изменение пользователя
     * @param request
     * @param userResource  Пользователь
     * @return
     */
    @PutMapping()
    ResponseEntity<DataResponse<UserResource>> updateUser(HttpServletRequest request, @RequestBody UserResource userResource) {
        LOG.info("PUT /api/user, RequestBody = {}", userResource);
        try {
            UserResource resource = userService.updateUser(userResource);
            DataResponse<UserResource> response = new DataResponse<>();
            response.setData(resource);
            ResponseEntity<DataResponse<UserResource>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<UserResource>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Удаление пользователя
     * @param request
     * @param userId    Код пользователя
     * @return
     */
    @DeleteMapping("{userId}")
    ResponseEntity<BaseResponse> deleteUser(HttpServletRequest request, @PathVariable Long userId) {
        LOG.info("DELETE: /api/user/{}", userId);
        try {
            userService.deleteUser(userId);
            ResponseEntity<BaseResponse> entity = new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<BaseResponse> result = new ResponseEntity<>(new BaseResponse(500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Генерация пароля и отправка его на Email
     * @param host
     * @param userResource  Пользователь
     * @return
     */
    @PutMapping("/gen-password")
    ResponseEntity<DataResponse<BaseResponse>> generatePasswordAndSendMail(@RequestHeader String host, @RequestBody UserResource userResource) {
        LOG.info("PUT /api/user/gen-password, RequestBody = {}", userResource);
        try {
            userService.sendNewPasswordToUserByMail(userResource.getEmail(), host);
            DataResponse<BaseResponse> response = new DataResponse<>();
            ResponseEntity<DataResponse<BaseResponse>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<BaseResponse>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }

    /**
     * Генерация пароля и отправка его на Email
     * @param host
     * @return
     */
    @PutMapping("/gen-passwords")
    ResponseEntity<DataResponse<BaseResponse>> generatePasswordAndSendMail(@RequestHeader String host) {
        LOG.info("PUT /api/user/gen-passwords");
        try {
            userService.sendNewPasswordToAllUsers(host);
            DataResponse<BaseResponse> response = new DataResponse<>();
            ResponseEntity<DataResponse<BaseResponse>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<BaseResponse>> result = new ResponseEntity<>(new DataResponse<>(null, 500, e.getMessage()), HttpStatus.BAD_REQUEST);
            return result;
        }
    }
    /**
     * Получить список Пользовательских ролей
     * @param request
     * @return
     */
    @GetMapping("/role")
    public ResponseEntity<DataResponse<List<RoleResource>>> getRoles(HttpServletRequest request) {
        LOG.debug("GET: /api/user/role");
        try {
            List<RoleResource> resource = userService.findRoles();
            DataResponse<List<RoleResource>> response = new DataResponse<>();
            if (resource != null) {
                response.setData(resource);
            }
            ResponseEntity<DataResponse<List<RoleResource>>> entity = new ResponseEntity<>(response, HttpStatus.OK);
            return entity;
        } catch (Exception e) {
            LOG.error("e={}, e.getMessage={}, e.getStackTrace={}", e, e.getMessage(), Arrays.toString(e.getStackTrace()));
            ResponseEntity<DataResponse<List<RoleResource>>> result = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return result;
        }
    }

}
