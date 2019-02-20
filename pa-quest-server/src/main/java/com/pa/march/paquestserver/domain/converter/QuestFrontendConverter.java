package com.pa.march.paquestserver.domain.converter;

import com.pa.march.paquestserver.domain.User;
import com.pa.march.paquestserver.message.resource.UserResource;
import org.springframework.stereotype.Component;

@Component
public class QuestFrontendConverter {

    public User convertUserResource(UserResource userResource) {
        User user = new User();
        Long id = userResource.getId();
        if (id != null && id.longValue() != 0L) {
            user.setId(id);
        }
        user.setUsername(userResource.getUsername());
        user.setName(userResource.getName());
        user.setEmail(userResource.getEmail());
        user.setPassword(userResource.getPassword());
        // TODO: Добавить конвертацию ролей!!!
        //user.setRoles();

        return user;

    }
}
