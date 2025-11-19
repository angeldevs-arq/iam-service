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

    private final RestClient client;

    @Value("${PROFILE_SERVICE_URL}")
    private String profileServiceUrl;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
        client = RestClient.create();
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

        /*
         * {
         * "firstName": "string",
         * "lastName": "string",
         * "email": "string",
         * "street": "string",
         * "number": "string",
         * "city": "string",
         * "postalCode": "string",
         * "country": "string",
         * "type": "string"
         * }
         */

        var profileBody = Map.of(
                "firstName", signUpResource.profile().firstName(),
                "lastName", signUpResource.profile().lastName(),
                "email", signUpResource.username(),
                "street", signUpResource.profile().street(),
                "number", signUpResource.profile().number(),
                "city", signUpResource.profile().city(),
                "postalCode", signUpResource.profile().postalCode(),
                "country", signUpResource.profile().country(),
                "type", signUpResource.profile().type());

        CompletableFuture.runAsync(() -> {
            client.post()
                    .uri(profileServiceUrl + "/api/v1/profiles")
                    .body(profileBody)
                    .retrieve()
                    .toBodilessEntity();
        });

        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }
}
