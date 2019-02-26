package com.pa.march.paquestserver.converter;

import com.pa.march.paquestserver.domain.User;
import com.pa.march.paquestserver.message.resource.UserResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserToResourceConverter extends ConversionServiceAwareConverter<User, UserResource> {

    @Override
    public UserResource convert(User user) {
        log.debug("user={}", user);
        UserResource userResource = new UserResource();
        BeanUtils.copyProperties(user, userResource);
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name().toUpperCase())
                .collect(Collectors.toSet());
        userResource.setRoles(roles);
        return userResource;
    }

}
