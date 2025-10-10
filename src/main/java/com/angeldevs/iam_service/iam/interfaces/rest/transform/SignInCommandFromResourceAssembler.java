package com.angeldevs.iam_service.iam.interfaces.rest.transform;

import com.angeldevs.iam_service.iam.domain.model.commands.SignInCommand;
import com.angeldevs.iam_service.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.username(), resource.password());
    }
}
