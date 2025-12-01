package com.angeldevs.iam_service.iam.interfaces.rest.transform;

import com.angeldevs.iam_service.iam.domain.model.commands.SignUpCommand;
import com.angeldevs.iam_service.iam.domain.model.valueobject.ERoleType;
import com.angeldevs.iam_service.iam.interfaces.rest.resources.SignUpResource;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        return new SignUpCommand(resource.username(), resource.password(), ERoleType.valueOf(resource.role()));
    }
}
