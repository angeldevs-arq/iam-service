package com.angeldevs.iam_service.iam.infrastructure.acl.profile.services;

import com.angeldevs.iam_service.iam.application.internal.outboundservices.acl.profile.CreateProfileCommand;
import com.angeldevs.iam_service.iam.application.internal.outboundservices.acl.profile.ProfileClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProfileClientImpl implements ProfileClient {
    private final RestClient restClient;
    private final String profileBaseUrl = "http://localhost:8082/api/v1/profiles";

    public ProfileClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void createProfile(CreateProfileCommand command) {
        restClient.post().uri(profileBaseUrl).body(command).retrieve()
                .onStatus(status-> !status.is2xxSuccessful(),
                        (request,response)->{
                            throw new RuntimeException("Error creating profile: " + response.getStatusCode());
                        }).toBodilessEntity();
    }
}
