package com.angeldevs.iam_service.iam.infrastructure.tokens.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import com.angeldevs.iam_service.iam.application.internal.outboundservices.tokens.TokenService;

public interface BearerTokenService extends TokenService {
    String getBearerTokenFrom(HttpServletRequest request);

    String generateToken(Authentication authentication);
}
