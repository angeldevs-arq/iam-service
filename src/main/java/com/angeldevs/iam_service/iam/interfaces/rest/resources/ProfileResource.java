package com.angeldevs.iam_service.iam.interfaces.rest.resources;

public record ProfileResource(
        String firstName,
        String lastName,
        String street,
        String number,
        String city,
        String postalCode,
        String country,
        String type) {
}