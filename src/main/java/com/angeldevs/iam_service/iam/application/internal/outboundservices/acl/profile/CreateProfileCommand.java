package com.angeldevs.iam_service.iam.application.internal.outboundservices.acl.profile;

public record CreateProfileCommand(
        String firstName,
        String lastName,
        String email,
        String street,
        String number,
        String city,
        String postalCode,
        String country,
        String profileImageUrl,
        String profileImagePublicId,
        String type,
        Long userId
) {
}
