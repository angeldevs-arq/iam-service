package com.angeldevs.iam_service.iam.domain.services;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.angeldevs.iam_service.iam.domain.model.aggregates.User;
import com.angeldevs.iam_service.iam.domain.model.commands.SignInCommand;
import com.angeldevs.iam_service.iam.domain.model.commands.SignUpCommand;

import java.util.Optional;

public interface UserCommandService {
    Optional<User> handle(SignUpCommand command);

    Optional<ImmutablePair<User, String>> handle(SignInCommand command);
}
