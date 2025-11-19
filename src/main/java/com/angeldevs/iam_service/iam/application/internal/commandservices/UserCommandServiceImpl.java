package com.angeldevs.iam_service.iam.application.internal.commandservices;

import com.angeldevs.iam_service.iam.application.internal.outboundservices.acl.profile.CreateProfileCommand;
import com.angeldevs.iam_service.iam.application.internal.outboundservices.acl.profile.ProfileClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import com.angeldevs.iam_service.iam.application.internal.outboundservices.hashing.HashingService;
import com.angeldevs.iam_service.iam.application.internal.outboundservices.tokens.TokenService;
import com.angeldevs.iam_service.iam.domain.model.aggregates.User;
import com.angeldevs.iam_service.iam.domain.model.commands.SignInCommand;
import com.angeldevs.iam_service.iam.domain.model.commands.SignUpCommand;
import com.angeldevs.iam_service.iam.domain.services.UserCommandService;
import com.angeldevs.iam_service.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final ProfileClient profileClient;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService,
            TokenService tokenService, ProfileClient profileClient) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.profileClient = profileClient;
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username()))
            throw new RuntimeException("Username already exists");

        var user = new User(command.username(), hashingService.encode(command.password()));
        userRepository.save(user);

        //CrearPerfil asociado al usuario
        CreateProfileCommand externalCommand = new CreateProfileCommand("","",
        user.getUsername(),"","","","","",
                "https://res.cloudinary.com/dtxv5wnbj/image/upload/v1762385933/default-profile_kxt5l2.jpg",
                "default-profile_kxt5l2","NONE",user.getId());

        profileClient.createProfile(externalCommand);

        return userRepository.findByUsername(command.username());
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!hashingService.matches(command.password(), user.getPassword()))
            throw new RuntimeException("Invalid password");
        var token = tokenService.generateToken(user.getUsername());
        return Optional.of(new ImmutablePair<>(user, token));
    }
}
