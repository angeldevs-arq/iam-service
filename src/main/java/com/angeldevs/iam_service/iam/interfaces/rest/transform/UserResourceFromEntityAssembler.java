package com.angeldevs.iam_service.iam.interfaces.rest.transform;

import com.angeldevs.iam_service.iam.domain.model.aggregates.User;
import com.angeldevs.iam_service.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User entity) {
        return new UserResource(entity.getId(), entity.getUsername(),entity.getRole().name());
    }
}
