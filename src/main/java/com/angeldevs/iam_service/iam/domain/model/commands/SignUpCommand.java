package com.angeldevs.iam_service.iam.domain.model.commands;

import com.angeldevs.iam_service.iam.domain.model.valueobject.ERoleType;

public record SignUpCommand(String username, String password, ERoleType role) {
}
