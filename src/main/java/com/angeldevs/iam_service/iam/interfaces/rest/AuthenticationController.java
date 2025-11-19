package com.angeldevs.iam_service.iam.interfaces.rest;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.angeldevs.iam_service.iam.domain.services.UserCommandService;
import com.angeldevs.iam_service.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.angeldevs.iam_service.iam.interfaces.rest.resources.SignInResource;
import com.angeldevs.iam_service.iam.interfaces.rest.resources.SignUpResource;
import com.angeldevs.iam_service.iam.interfaces.rest.resources.UserResource;
import com.angeldevs.iam_service.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.angeldevs.iam_service.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.angeldevs.iam_service.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.angeldevs.iam_service.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v1/iam", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {
    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource signInResource) {
        var signInCommand = SignInCommandFromResourceAssembler.toCommandFromResource(signInResource);
        var authenticatedUser = userCommandService.handle(signInCommand);
        if (authenticatedUser.isEmpty())
            return ResponseEntity.notFound().build();
        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler
                .toResourceFromEntity(authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());
        return ResponseEntity.ok(authenticatedUserResource);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResource> signUp(@RequestBody SignUpResource signUpResource) {
        var signUpCommand = SignUpCommandFromResourceAssembler.toCommandFromResource(signUpResource);
        var user = userCommandService.handle(signUpCommand);
        if (user.isEmpty())
            return ResponseEntity.badRequest().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }
}
