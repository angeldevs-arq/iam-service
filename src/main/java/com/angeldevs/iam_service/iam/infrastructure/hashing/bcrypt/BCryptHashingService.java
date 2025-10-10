package com.angeldevs.iam_service.iam.infrastructure.hashing.bcrypt;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.angeldevs.iam_service.iam.application.internal.outboundservices.hashing.HashingService;

public interface BCryptHashingService extends HashingService, PasswordEncoder {

}
