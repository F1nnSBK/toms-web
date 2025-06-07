package com.toms.app.security.apikey;

import org.springframework.beans.factory.annotation.Value; // Für API Key aus application.properties
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthManager implements AuthenticationManager {

    @Value("${api.key.secret}")
    private String apiSecretKey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String providedApiKey = ((ApiKeyAuthentication) authentication).getApiKey();

        if (apiSecretKey.equals(providedApiKey)) {
            return new ApiKeyAuthentication(providedApiKey, AuthorityUtils.createAuthorityList("ROLE_API_USER"));
        } else {
            throw new BadCredentialsException("Invalid API Key");
        }
    }
}